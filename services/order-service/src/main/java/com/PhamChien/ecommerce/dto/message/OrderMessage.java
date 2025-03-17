package com.PhamChien.ecommerce.dto.message;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class OrderMessage {
    private String orderId;
    private String status;
}
