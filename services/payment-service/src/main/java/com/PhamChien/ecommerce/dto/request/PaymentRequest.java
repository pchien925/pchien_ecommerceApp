package com.PhamChien.ecommerce.dto.request;

import lombok.Getter;

@Getter
public class PaymentRequest {
    private int amount;
    private String orderInfo;
}
