package com.PhamChien.ecommerce.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class OrderItem {
    private Long productId;

    private String productName;

    private Double productPrice;

    private String productSize;

    private String productColor;

    private String productImage;

    private Long quantity;
}
