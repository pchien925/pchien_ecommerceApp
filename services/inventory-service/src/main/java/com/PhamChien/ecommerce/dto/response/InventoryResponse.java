package com.PhamChien.ecommerce.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
public class InventoryResponse {
    private Long id;
    private Long productId;
    private Long availableQuantity;
    private Long reservedQuantity;
}
