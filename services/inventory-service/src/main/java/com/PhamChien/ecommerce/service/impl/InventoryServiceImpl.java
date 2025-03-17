package com.PhamChien.ecommerce.service.impl;

import com.PhamChien.ecommerce.client.OrderClient;
import com.PhamChien.ecommerce.domain.Inventory;
import com.PhamChien.ecommerce.dto.request.InventoryCreationRequest;
import com.PhamChien.ecommerce.dto.request.InventoryUpdateRequest;
import com.PhamChien.ecommerce.dto.response.InventoryResponse;
import com.PhamChien.ecommerce.dto.response.OrderItem;
import com.PhamChien.ecommerce.dto.response.OrderMessage;
import com.PhamChien.ecommerce.exception.InvalidDataException;
import com.PhamChien.ecommerce.mapper.InventoryMapper;
import com.PhamChien.ecommerce.repository.InventoryRepository;
import com.PhamChien.ecommerce.service.InventoryService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;
    private final OrderClient orderClient;

    @Override
    public InventoryResponse findByProductId(Long productId){
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new InvalidDataException("Product does not exist"));
        return inventoryMapper.toInventoryResponse(inventory);
    }

    @Override
    public InventoryResponse addInventory(InventoryCreationRequest request) {
        Inventory inventory = inventoryMapper.toInventory(request);
        return inventoryMapper.toInventoryResponse(inventoryRepository.save(inventory));
    }

    @Override
    public InventoryResponse updateInventory(Long productId, InventoryUpdateRequest request) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new InvalidDataException("Product does not exist"));
        inventoryMapper.updateInventory(inventory, request);
        return inventoryMapper.toInventoryResponse(inventoryRepository.save(inventory));
    }

    @Override
    public String deleteInventorys(List<Long> productIds) {
        inventoryRepository.deleteAllByIdInBatch(productIds);
        return "deleted";
    }

    @Override
    public List<InventoryResponse> updateReservedQuantity(List<OrderItem> orderItems){
        List<InventoryResponse> list = new ArrayList<>();

        for (OrderItem orderItem : orderItems) {
            Inventory inventory = inventoryRepository.findByProductId(orderItem.getProductId())
                    .orElseThrow(() -> new InvalidDataException("Product does not exist"));
            inventory.setAvailableQuantity(inventory.getAvailableQuantity() - orderItem.getQuantity());
            inventory.setReservedQuantity(inventory.getReservedQuantity() + orderItem.getQuantity());
            list.add(inventoryMapper.toInventoryResponse(inventoryRepository.save(inventory)));
        }
        return list;
    }


    @KafkaListener(topics = "order-created-topic", groupId = "order-created-group")
    public void createInventory(String message){
        Gson gson = new Gson();
        OrderMessage orderMessage = gson.fromJson(message, OrderMessage.class);

        List<OrderItem> orderItems = orderClient.getAll(orderMessage.getOrderId()).getData();

        for (OrderItem orderItem : orderItems) {
            Inventory inventory = inventoryRepository.findByProductId(orderItem.getProductId())
                    .orElseThrow(() -> new InvalidDataException("Product does not exist"));
            inventory.setAvailableQuantity(inventory.getAvailableQuantity() - orderItem.getQuantity());
            inventory.setReservedQuantity(inventory.getReservedQuantity() + orderItem.getQuantity());
            inventoryRepository.save(inventory);
        }
    }

    @KafkaListener(topics = "order-updated-topic", groupId = "order-updated-group")
    public void updateInventory(String message){
        Gson gson = new Gson();
        OrderMessage orderMessage = gson.fromJson(message, OrderMessage.class);

        if (orderMessage.getStatus().equals("CANCELLED") || orderMessage.getStatus().equals("FAILED")) {
            List<OrderItem> orderItems = orderClient.getAll(orderMessage.getOrderId()).getData();

            for (OrderItem orderItem : orderItems) {
                Inventory inventory = inventoryRepository.findByProductId(orderItem.getProductId())
                        .orElseThrow(() -> new InvalidDataException("Product does not exist"));
                inventory.setAvailableQuantity(inventory.getAvailableQuantity() + orderItem.getQuantity());
                inventory.setReservedQuantity(inventory.getReservedQuantity() - orderItem.getQuantity());
                inventoryRepository.save(inventory);
            }
        }
        else {
            List<OrderItem> orderItems = orderClient.getAll(orderMessage.getOrderId()).getData();

            for (OrderItem orderItem : orderItems) {
                Inventory inventory = inventoryRepository.findByProductId(orderItem.getProductId())
                        .orElseThrow(() -> new InvalidDataException("Product does not exist"));
                inventory.setReservedQuantity(inventory.getReservedQuantity() - orderItem.getQuantity());
                inventoryRepository.save(inventory);
            }
        }
    }



}
