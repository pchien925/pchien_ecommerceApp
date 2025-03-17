package com.PhamChien.ecommerce.service;

import com.PhamChien.ecommerce.dto.request.CartItemRequest;
import com.PhamChien.ecommerce.dto.response.CartResponse;

public interface CartService {

    CartResponse getCartByUserId();

    CartResponse addItem(CartItemRequest request);

    CartResponse updateCart(Long productId, Long quantity);

    CartResponse removeItem(Long productId);

    CartResponse clearCart();
}
