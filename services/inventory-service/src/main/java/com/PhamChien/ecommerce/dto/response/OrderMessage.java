package com.PhamChien.ecommerce.dto.response;


import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class OrderMessage {
    private String orderId;
    private String status;
}
