package com.PhamChien.ecommerce.dto.request;

import lombok.Getter;

@Getter
public class CartItemRequest {
    Long productId;
    Long quantity;
}
