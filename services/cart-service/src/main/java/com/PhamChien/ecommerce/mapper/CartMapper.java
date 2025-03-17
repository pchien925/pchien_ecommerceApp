package com.PhamChien.ecommerce.mapper;

import com.PhamChien.ecommerce.domain.Cart;
import com.PhamChien.ecommerce.dto.response.CartResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartResponse toCartResponse(Cart cart);
    Cart toCart(CartResponse response);

}
