package com.PhamChien.ecommerce.controller;

import com.PhamChien.ecommerce.domain.OrderItem;
import com.PhamChien.ecommerce.dto.request.OrderRequest;
import com.PhamChien.ecommerce.dto.response.ApiResponse;
import com.PhamChien.ecommerce.dto.response.OrderResponse;
import com.PhamChien.ecommerce.dto.response.PageResponse;
import com.PhamChien.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    ApiResponse<OrderResponse> create(@RequestBody OrderRequest request){
        return ApiResponse.<OrderResponse>builder()
                .status(HttpStatus.CREATED.value())
                .data(orderService.create(request))
                .build();
    }

    @GetMapping("/{orderId}/items")
    ApiResponse<List<OrderItem>> getAll(@PathVariable String orderId){
        return ApiResponse.<List<OrderItem>>builder()
                .status(HttpStatus.OK.value())
                .data(orderService.getOrderItemsByOrderId(orderId))
                .build();
    }

    @GetMapping("/{orderId}")
    ApiResponse<OrderResponse> getById(@PathVariable String orderId){
        return ApiResponse.<OrderResponse>builder()
                .status(HttpStatus.OK.value())
                .data(orderService.getOrderById(orderId))
                .build();
    }

    @PutMapping("/{orderId}")
    ApiResponse<OrderResponse> update(@PathVariable String orderId, @RequestBody OrderRequest request){
        return ApiResponse.<OrderResponse>builder()
                .status(HttpStatus.OK.value())
                .data(orderService.updateOrder(orderId, request))
                .build();
    }

    @DeleteMapping("/{orderId}")
    ApiResponse<String> delete(@PathVariable String orderId){
        return ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .data(orderService.deleteOrder(orderId))
                .build();
    }

    @PutMapping("/{orderId}/status")
    ApiResponse<OrderResponse> updateStatus(@PathVariable String orderId, @RequestParam String status){
        return ApiResponse.<OrderResponse>builder()
                .status(HttpStatus.OK.value())
                .data(orderService.updateStatus(orderId, status))
                .build();
    }

    @GetMapping("/user/{userId}")
    ApiResponse<PageResponse<OrderResponse>> getByUserId(@PathVariable String userId,
                                                         @RequestParam(defaultValue = "1") int page,
                                                         @RequestParam(defaultValue = "10") int size){
        return ApiResponse.<PageResponse<OrderResponse>>builder()
                .status(HttpStatus.OK.value())
                .data(orderService.getOrdersByUserId(userId, page, size))
                .build();
    }
    
    @GetMapping("all")
    ApiResponse<PageResponse<OrderResponse>> getAll(@RequestParam(defaultValue = "1") int page,
                                                    @RequestParam(defaultValue = "5") int size){
        return ApiResponse.<PageResponse<OrderResponse>>builder()
                .status(HttpStatus.OK.value())
                .data(orderService.getAll(page, size))
                .build();
    }
}
