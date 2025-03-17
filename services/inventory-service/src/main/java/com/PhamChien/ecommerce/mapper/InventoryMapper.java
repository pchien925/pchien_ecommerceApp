package com.PhamChien.ecommerce.mapper;

import com.PhamChien.ecommerce.domain.Inventory;
import com.PhamChien.ecommerce.dto.request.InventoryCreationRequest;
import com.PhamChien.ecommerce.dto.request.InventoryUpdateRequest;
import com.PhamChien.ecommerce.dto.response.InventoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface InventoryMapper {
    Inventory toInventory(InventoryCreationRequest request);
    InventoryResponse toInventoryResponse(Inventory inventory);

    void updateInventory(@MappingTarget Inventory inventory, InventoryUpdateRequest request);
}
