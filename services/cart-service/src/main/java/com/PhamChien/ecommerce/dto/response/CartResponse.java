package com.PhamChien.ecommerce.dto.response;

import com.PhamChien.ecommerce.domain.CartItem;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Set;

@Getter
@Setter
@Builder
public class CartResponse {
    private String id;

    private String userId;

    private Set<CartItem> cartItems;

    private Long totalPromotionalPrice;

    private Long totalPrice;

}
