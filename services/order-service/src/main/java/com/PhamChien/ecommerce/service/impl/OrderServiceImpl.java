package com.PhamChien.ecommerce.service.impl;

import com.PhamChien.ecommerce.domain.Order;
import com.PhamChien.ecommerce.domain.OrderItem;
import com.PhamChien.ecommerce.domain.enumeration.OrderStatus;
import com.PhamChien.ecommerce.dto.message.OrderMessage;
import com.PhamChien.ecommerce.dto.request.OrderRequest;
import com.PhamChien.ecommerce.dto.response.OrderResponse;
import com.PhamChien.ecommerce.dto.response.PageResponse;
import com.PhamChien.ecommerce.exception.ResourceNotFoundException;
import com.PhamChien.ecommerce.mapper.OrderMapper;
import com.PhamChien.ecommerce.repository.OrderRepository;
import com.PhamChien.ecommerce.service.OrderService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.topic.orderCreated}")
    private String orderCreatedTopic;

    @Value("${spring.kafka.topic.orderUpdated}")
    private String orderUpdatedTopic;

    @Value("${spring.kafka.topic.orderMail}")
    private String orderMailTopic;


    @Override
    public OrderResponse create(OrderRequest request){
        Optional<Order> oldOrder = orderRepository.findByUserIdAndStatusIn(request.getUserId(), List.of(OrderStatus.NEW));
        oldOrder.ifPresent(orderRepository::delete);

        Order newOrder = orderMapper.toOrder(request);
        newOrder.setCreatedAt(LocalDateTime.now());
        newOrder.setUpdatedAt(LocalDateTime.now());
        newOrder.setStatus(OrderStatus.NEW);

        Order order = orderRepository.save(newOrder);

        OrderMessage orderMessage = OrderMessage.builder()
                .orderId(order.getId())
                .status(order.getStatus().name())
                .build();
        Gson gson = new Gson();
        String json = gson.toJson(orderMessage);
        kafkaTemplate.send(orderCreatedTopic, json);
       return orderMapper.toOrderResponse(order );
    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(String orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order Not Found"));
        return order.getOrderItems();
    }

    @Override
    public OrderResponse updateOrder(String orderId, OrderRequest request){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order Not Found"));
        order.setProfileId(request.getProfileId());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setNotes(request.getNotes());
        orderRepository.save(order);


        return orderMapper.toOrderResponse(order);
    }

    @Override
    public String deleteOrder(String orderId){
        orderRepository.deleteById(orderId);
        return orderId;
    }

    @Override
    public OrderResponse updateStatus(String orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order Not Found"));
        order.setStatus(OrderStatus.valueOf(status));
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
        OrderMessage orderMessage = OrderMessage.builder()
                .orderId(order.getId())
                .status(order.getStatus().name())
                .build();
        if (Objects.equals(status, "SHIPPING") || Objects.equals(status, "COMPLETED"))
        {
            return orderMapper.toOrderResponse(order);
        }
        Gson gson = new Gson();
        String json = gson.toJson(orderMessage);
        kafkaTemplate.send(orderUpdatedTopic, json);
        return orderMapper.toOrderResponse(order);
    }

    @Override
    public OrderResponse getOrderById(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order Not Found"));
        return orderMapper.toOrderResponse(order);
    }

    @KafkaListener(topics = "checkout-order-topic", groupId = "checkout-order-group")
    public void checkoutOrder(String message){
        Gson gson = new Gson();
        OrderMessage orderMessage = gson.fromJson(message, OrderMessage.class);
        Order order = orderRepository.findById(orderMessage.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order Not Found"));
        if (orderMessage.getStatus().equals("00")){
            order.setStatus(OrderStatus.PAID);
            order.setUpdatedAt(LocalDateTime.now());

        } else {
            order.setStatus(OrderStatus.FAILED);
            order.setUpdatedAt(LocalDateTime.now());
        }
        orderRepository.save(order);
        OrderMessage sendMessage = OrderMessage.builder()
                .orderId(order.getId())
                .status(order.getStatus().name())
                .build();
        String json = gson.toJson(sendMessage);
        kafkaTemplate.send(orderMailTopic, order.getUserId());
        kafkaTemplate.send(orderUpdatedTopic, json);
    }

    @Override
    public PageResponse<OrderResponse> getOrdersByUserId(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());

        Page<Order> products = orderRepository.findByUserId(userId, pageable);
        return PageResponse.<OrderResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(products.getTotalPages())
                .totalElements(products.getTotalElements())
                .content(products.getContent().stream().map(orderMapper::toOrderResponse).toList())
                .build();
    }

    @Override
    public PageResponse<OrderResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());

        Page<Order> products = orderRepository.findAll( pageable);
        return PageResponse.<OrderResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(products.getTotalPages())
                .totalElements(products.getTotalElements())
                .content(products.getContent().stream().map(orderMapper::toOrderResponse).toList())
                .build();
    }
}
