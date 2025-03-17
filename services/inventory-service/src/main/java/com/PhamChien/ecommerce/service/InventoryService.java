package com.PhamChien.ecommerce.service;

import com.PhamChien.ecommerce.dto.request.InventoryCreationRequest;
import com.PhamChien.ecommerce.dto.request.InventoryUpdateRequest;
import com.PhamChien.ecommerce.dto.response.InventoryResponse;
import com.PhamChien.ecommerce.dto.response.OrderItem;

import java.util.List;

public interface InventoryService {
    InventoryResponse findByProductId(Long productId);

    InventoryResponse addInventory(InventoryCreationRequest request);

    InventoryResponse updateInventory(Long productId, InventoryUpdateRequest request);

    String deleteInventorys(List<Long> productIds);

    List<InventoryResponse> updateReservedQuantity(List<OrderItem> orderItems);

}
