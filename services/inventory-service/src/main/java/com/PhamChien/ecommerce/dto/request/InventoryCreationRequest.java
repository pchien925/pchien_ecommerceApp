package com.PhamChien.ecommerce.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class InventoryCreationRequest {
    @NotEmpty(message = "product not empty")
    private long productId;

    private long availableQuantity;

    private long reservedQuantity;

}
