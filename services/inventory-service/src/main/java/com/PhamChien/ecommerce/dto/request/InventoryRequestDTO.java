package com.PhamChien.ecommerce.dto.request;

import lombok.Getter;

@Getter
public class InventoryRequestDTO {
    private String productId;
    private long quantity;
}
