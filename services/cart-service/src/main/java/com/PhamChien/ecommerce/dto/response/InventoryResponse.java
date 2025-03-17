package com.PhamChien.ecommerce.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class InventoryResponse {
    private Long id;
    private Long productId;
    private Long availableQuantity;
    private Long reservedQuantity;
}
