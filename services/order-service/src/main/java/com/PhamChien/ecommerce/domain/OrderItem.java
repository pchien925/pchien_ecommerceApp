package com.PhamChien.ecommerce.domain;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class OrderItem {
    private Long productId;

    private String productName;

    private Double productPrice;

    private Double productPromotionalPrice;

    private String productSize;

    private String productColor;

    private String productThumbnailUrl;

    private Long quantity;
}
