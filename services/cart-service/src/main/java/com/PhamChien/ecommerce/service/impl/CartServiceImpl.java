package com.PhamChien.ecommerce.service.impl;

import com.PhamChien.ecommerce.client.InventoryClient;
import com.PhamChien.ecommerce.client.ProductClient;
import com.PhamChien.ecommerce.domain.Cart;
import com.PhamChien.ecommerce.domain.CartItem;
import com.PhamChien.ecommerce.dto.request.CartItemRequest;
import com.PhamChien.ecommerce.dto.response.CartResponse;
import com.PhamChien.ecommerce.dto.response.InventoryResponse;
import com.PhamChien.ecommerce.dto.response.ProductResponse;
import com.PhamChien.ecommerce.exception.InvalidDataException;
import com.PhamChien.ecommerce.exception.ResourceNotFoundException;
import com.PhamChien.ecommerce.mapper.CartItemMapper;
import com.PhamChien.ecommerce.mapper.CartMapper;
import com.PhamChien.ecommerce.repository.CartRepository;
import com.PhamChien.ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final CartItemMapper cartItemMapper;
    private final ProductClient productClient;
    private final InventoryClient inventoryClient;

    @Override
    public CartResponse getCartByUserId(){
        String userId = getUserId();

        Cart myCart = cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setTotalPromotionalPrice(0.0);
            cart.setTotalPrice(0.0);
            cart.setCreatedAt((new Date()));
            cart.setUpdatedAt((new Date()));
            return cartRepository.save(cart);
        });
        return cartMapper.toCartResponse(myCart);
    }

    @Override
    public CartResponse addItem(CartItemRequest request){
        String userId = getUserId();

        Cart cart = cartMapper.toCart(getCartByUserId());



        Optional<CartItem> existedItem = cart.getCartItems().stream()
                .filter(item -> item.getProductId().equals(request.getProductId()))
                .findFirst();

        if(existedItem.isPresent()){

            CartItem cartItem = existedItem.get();
            validateItem(request.getProductId(), cartItem.getQuantity() + request.getQuantity());
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
            cart.setTotalPromotionalPrice(cart.getTotalPromotionalPrice() + cartItem.getProductPromotionalPrice() * request.getQuantity());
            cart.setTotalPrice(cart.getTotalPrice() + cartItem.getProductPrice() * request.getQuantity());
        }
        else {
            ProductResponse product = productClient.getProduct(request.getProductId()).getData();
            CartItem cartItem = cartItemMapper.toCartItem(product);
            cartItem.setQuantity(request.getQuantity());
            cart.getCartItems().add(cartItem);
            cart.setTotalPromotionalPrice(cart.getTotalPromotionalPrice() + product.getPromotionalPrice() * request.getQuantity());
            cart.setTotalPrice(cart.getTotalPrice() + product.getPrice() * request.getQuantity());
        }
        cart.setUpdatedAt((new Date()));
        return cartMapper.toCartResponse(cartRepository.save(cart));
    }

    @Override
    public CartResponse updateCart(Long productId, Long quantity){
        String userId = getUserId();
        Cart cart = cartMapper.toCart(getCartByUserId());
        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new InvalidDataException("Product not found"));
        validateItem(productId, quantity);
        cart.setTotalPromotionalPrice(cart.getTotalPromotionalPrice() + (quantity - cartItem.getQuantity()) * cartItem.getProductPromotionalPrice());
        cart.setTotalPrice(cart.getTotalPrice() + (quantity - cartItem.getQuantity()) * cartItem.getProductPrice());
        cartItem.setQuantity(quantity);
        cart.setUpdatedAt((new Date()));
        return cartMapper.toCartResponse(cartRepository.save(cart));
    }


    @Override
    public CartResponse removeItem(Long productId){
        Cart cart = cartMapper.toCart(getCartByUserId());
        CartItem cartItem = cart.getCartItems().stream()
                        .filter(item -> item.getProductId().equals(productId))
                                .findFirst()
                .orElseThrow(() -> new InvalidDataException("Product not found"));
        cart.setTotalPromotionalPrice(cart.getTotalPromotionalPrice() - cartItem.getProductPromotionalPrice() * cartItem.getQuantity());
        cart.setTotalPrice(cart.getTotalPrice() - cartItem.getProductPrice() * cartItem.getQuantity());
        cart.getCartItems().remove(cartItem);
        cart.setUpdatedAt(new Date());
        return cartMapper.toCartResponse(cartRepository.save(cart));
    }

    @Override
    public CartResponse clearCart(){
        Cart cart = cartMapper.toCart(getCartByUserId());
        cart.setCartItems(null);
        cart.setTotalPrice(0.0);
        cart.setTotalPromotionalPrice(0.0);
        cart.setUpdatedAt(new Date());
        return cartMapper.toCartResponse(cartRepository.save(cart));
    }



    private String getUserId(){
        Jwt principal = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getClaimAsString("accountID");
    }

    private void validateItem(Long productId, Long quantity){
        ProductResponse product = productClient.getProduct(productId).getData();

        if(product == null){
            throw new ResourceNotFoundException("Product not found");
        }

        InventoryResponse productInventory = inventoryClient.getInventory(productId).getData();
        if(productInventory.getAvailableQuantity() - quantity < 0){
            throw new InvalidDataException("Not enough stock available");
        }
    }

}
