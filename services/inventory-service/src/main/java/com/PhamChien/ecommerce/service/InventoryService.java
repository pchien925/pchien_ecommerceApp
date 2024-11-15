package com.PhamChien.ecommerce.service;

import com.PhamChien.ecommerce.dto.request.InventoryRequestDTO;
import com.PhamChien.ecommerce.dto.response.InventoryResponse;

public interface InventoryService {
    InventoryResponse addInventory(InventoryRequestDTO request);

    InventoryResponse updateInventory(InventoryRequestDTO request);
}
