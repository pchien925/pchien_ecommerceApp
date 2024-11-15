package com.PhamChien.ecommerce.service.impl;

import com.PhamChien.ecommerce.domain.Inventory;
import com.PhamChien.ecommerce.dto.request.InventoryRequestDTO;
import com.PhamChien.ecommerce.dto.response.InventoryResponse;
import com.PhamChien.ecommerce.exception.InvalidDataException;
import com.PhamChien.ecommerce.mapper.InventoryMapper;
import com.PhamChien.ecommerce.repository.InventoryRepository;
import com.PhamChien.ecommerce.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;

    @Override
    public InventoryResponse addInventory(InventoryRequestDTO request) {
        if (inventoryRepository.existsByProductId(request.getProductId())) {
            throw new InvalidDataException("Product already exists");
        }
        return inventoryMapper.toInventoryResponse(inventoryRepository.save(inventoryMapper.toInventory(request)));
    }

    @Override
    public InventoryResponse updateInventory(InventoryRequestDTO request) {
        Inventory inventory = inventoryRepository.findByProductId(request.getProductId());
        inventory.setQuantity(request.getQuantity());
        return inventoryMapper.toInventoryResponse(inventoryRepository.save(inventory));
    }


}
