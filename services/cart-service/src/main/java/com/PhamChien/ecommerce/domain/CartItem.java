package com.PhamChien.ecommerce.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class CartItem {
    private Long productId;

    private String productName;

    private String productDescription;

    private String productSlug;

    private Double productPrice;

    private Double productPromotionalPrice;

    private String productSize;

    private String productColor;

    private Integer productSold;

    private String productThumbnailUrl;

    private Long quantity;
}
