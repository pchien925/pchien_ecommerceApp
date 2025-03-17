package com.PhamChien.ecommerce.controller;


import com.PhamChien.ecommerce.dto.request.PaymentRequest;
import com.PhamChien.ecommerce.dto.response.ApiResponse;
import com.PhamChien.ecommerce.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;


@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/vnpay")
    public ApiResponse<String> createPayment(@RequestBody PaymentRequest req, HttpServletRequest request) throws UnsupportedEncodingException {
        return ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .data(paymentService.createPayment(req, request))
                .build();
    }

    @GetMapping("/vnpay/return")
    public ApiResponse<String> vnpayReturn(@RequestParam("vnp_ResponseCode") String responseCode,
                                           @RequestParam("vnp_OrderInfo") String orderInfo
                                           ) {
        return ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .data(paymentService.vnpayReturn(responseCode, orderInfo))
                .build();
    }

}
