package com.PhamChien.ecommerce.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    NEW,
    PENDING,
    PAID,
    FAILED,
    CANCELLED,
    SHIPPING,
    COMPLETED
}