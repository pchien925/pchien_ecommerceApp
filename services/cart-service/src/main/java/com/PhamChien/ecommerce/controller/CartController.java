package com.PhamChien.ecommerce.controller;

import com.PhamChien.ecommerce.dto.request.CartItemRequest;
import com.PhamChien.ecommerce.dto.response.ApiResponse;
import com.PhamChien.ecommerce.dto.response.CartResponse;
import com.PhamChien.ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
@Slf4j
public class CartController {
    private final CartService cartService;


    @PostMapping("/items")
    ApiResponse<CartResponse> addItems(@RequestBody CartItemRequest request){
        return ApiResponse.<CartResponse>builder()
                .status(HttpStatus.OK.value())
                .data(cartService.addItem(request))
                .build();
    }

    @GetMapping("/myCart")
    ApiResponse<CartResponse> getMyCart(){
        return ApiResponse.<CartResponse>builder()
                .status(HttpStatus.OK.value())
                .data(cartService.getCartByUserId())
                .build();
    }

    @PutMapping("/items/{productId}")
    ApiResponse<CartResponse> updateItems(@PathVariable Long productId, @RequestParam Long quantity){
        return ApiResponse.<CartResponse>builder()
                .status(HttpStatus.OK.value())
                .data(cartService.updateCart(productId, quantity))
                .build();
    }

    @DeleteMapping("/items/{productId}")
    ApiResponse<CartResponse> deleteItems(@PathVariable Long productId){
        return ApiResponse.<CartResponse>builder()
                .status(HttpStatus.OK.value())
                .data(cartService.removeItem(productId))
                .build();
    }

    @DeleteMapping("/clear")
    ApiResponse<CartResponse> clearCart(){
        return ApiResponse.<CartResponse>builder()
                .status(HttpStatus.OK.value())
                .data(cartService.clearCart())
                .build();
    }

}
