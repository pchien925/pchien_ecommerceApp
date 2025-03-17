package com.PhamChien.ecommerce.domain;

import com.PhamChien.ecommerce.domain.enumeration.OrderStatus;
import lombok.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(value = "order")
public class Order {
    @MongoId
    private String id;

    private String userId;

    private Long profileId;

    private Long amount;

    private OrderStatus status;

    private String paymentMethod;

    private String notes;
    @Builder.Default
    private List<OrderItem> orderItems = new ArrayList<>();


    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
