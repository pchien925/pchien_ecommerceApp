package com.PhamChien.ecommerce.mapper;

import com.PhamChien.ecommerce.domain.Inventory;
import com.PhamChien.ecommerce.dto.request.InventoryRequestDTO;
import com.PhamChien.ecommerce.dto.response.InventoryResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InventoryMapper {
    Inventory toInventory(InventoryRequestDTO requestDTO);
    InventoryResponse toInventoryResponse(Inventory inventory);
}
