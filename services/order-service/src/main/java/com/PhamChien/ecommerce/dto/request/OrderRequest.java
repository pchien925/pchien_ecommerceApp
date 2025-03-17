package com.PhamChien.ecommerce.dto.request;

import com.PhamChien.ecommerce.domain.OrderItem;
import com.PhamChien.ecommerce.domain.enumeration.OrderStatus;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
public class OrderRequest {
    private String userId;

    private Long profileId;

    private String paymentMethod;

    private String notes;

    private List<OrderItem> orderItems;


    private Long amount;

}
