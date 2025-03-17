package com.PhamChien.ecommerce.client;

import com.PhamChien.ecommerce.dto.response.ApiResponse;
import com.PhamChien.ecommerce.dto.response.OrderItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "order-service", url = "http://localhost:8091/api/v1/orders")
public interface OrderClient {
    @GetMapping("/{orderId}/items")
    ApiResponse<List<OrderItem>> getAll(@PathVariable String orderId);
}
