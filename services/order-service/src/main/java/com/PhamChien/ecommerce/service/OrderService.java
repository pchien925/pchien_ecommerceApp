package com.PhamChien.ecommerce.service;

import com.PhamChien.ecommerce.domain.OrderItem;
import com.PhamChien.ecommerce.dto.request.OrderRequest;
import com.PhamChien.ecommerce.dto.response.OrderResponse;
import com.PhamChien.ecommerce.dto.response.PageResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {
    OrderResponse create(OrderRequest request);

    List<OrderItem> getOrderItemsByOrderId(String orderId);

    OrderResponse updateOrder(String orderId, OrderRequest request);

    String deleteOrder(String orderId);

    OrderResponse updateStatus(String orderId, String status);

    OrderResponse getOrderById(String orderId);

    PageResponse<OrderResponse> getOrdersByUserId(String userId, int page, int size);

    PageResponse<OrderResponse> getAll(int page, int size);
}
