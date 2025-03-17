package com.PhamChien.ecommerce.dto.request;

import lombok.Getter;

@Getter
public class InventoryUpdateRequest {
    private long availableQuantity;

    private long reservedQuantity;
}
