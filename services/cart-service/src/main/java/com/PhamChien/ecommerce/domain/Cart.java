package com.PhamChien.ecommerce.domain;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(value = "cart")
public class Cart {
    @MongoId
    private String id;

    private String userId;

    @Builder.Default
    private List<CartItem> cartItems = new ArrayList<>();

    private Double totalPromotionalPrice;

    private Double totalPrice;

    private Date createdAt;

    private Date updatedAt;
}
