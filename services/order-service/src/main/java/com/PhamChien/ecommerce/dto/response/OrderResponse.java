package com.PhamChien.ecommerce.dto.response;

import com.PhamChien.ecommerce.domain.OrderItem;
import com.PhamChien.ecommerce.domain.enumeration.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
public class OrderResponse {
    private String id;

    private String userId;

    private String profileId;

    private Long amount;

    private OrderStatus status;

    private String paymentMethod;

    @Builder.Default
    List<OrderItem> orderItems = new ArrayList<>();

    private String notes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
