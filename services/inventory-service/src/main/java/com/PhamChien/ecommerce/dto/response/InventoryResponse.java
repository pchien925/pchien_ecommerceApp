package com.PhamChien.ecommerce.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
public class InventoryResponse {
    private Long id;
    private String productId;
    private Long quantity;
}
