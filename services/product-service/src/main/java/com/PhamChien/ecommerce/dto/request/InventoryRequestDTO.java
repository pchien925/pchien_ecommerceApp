package com.PhamChien.ecommerce.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InventoryRequestDTO {
    private Long productId;
    private long quantity;
}
