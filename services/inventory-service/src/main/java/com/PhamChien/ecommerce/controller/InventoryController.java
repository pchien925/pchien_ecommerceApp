package com.PhamChien.ecommerce.controller;

import com.PhamChien.ecommerce.dto.request.InventoryCreationRequest;
import com.PhamChien.ecommerce.dto.request.InventoryUpdateRequest;
import com.PhamChien.ecommerce.dto.response.ApiResponse;
import com.PhamChien.ecommerce.dto.response.InventoryResponse;
import com.PhamChien.ecommerce.dto.response.OrderItem;
import com.PhamChien.ecommerce.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping("/{productId}")
    ApiResponse<InventoryResponse> getInventory(@PathVariable("productId") Long productId) {
        return ApiResponse.<InventoryResponse>builder()
                .status(HttpStatus.OK.value())
                .data(inventoryService.findByProductId(productId))
                .build();
    }

    @PostMapping
    ApiResponse<InventoryResponse> addInventory(@RequestBody InventoryCreationRequest request) {
        return ApiResponse.<InventoryResponse>builder()
                .status(HttpStatus.OK.value())
                .data(inventoryService.addInventory(request))
                .build();
    }

    @PutMapping("/{productId}")
    ApiResponse<InventoryResponse> updateInventory(@PathVariable("productId") Long productId, @RequestBody InventoryUpdateRequest request) {
        return ApiResponse.<InventoryResponse>builder()
                .status(HttpStatus.OK.value())
                .data(inventoryService.updateInventory(productId, request))
                .build();
    }

    @DeleteMapping
    ApiResponse<String> deleteInventory(@RequestBody List<Long> productIds) {
        return ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .data(inventoryService.deleteInventorys(productIds))
                .build();
    }

    @PostMapping("/updateStockForOrder")
    ApiResponse<List<InventoryResponse>> updateStockForOrder(@RequestBody List<OrderItem> orderItems) {
        return ApiResponse.<List<InventoryResponse>>builder()
                .status(HttpStatus.OK.value())
                .data(inventoryService.updateReservedQuantity(orderItems))
                .build();
    }

}
