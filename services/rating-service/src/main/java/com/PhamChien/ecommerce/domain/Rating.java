package com.PhamChien.ecommerce.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rating")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private Long productId;

    private String content;

    private int ratingStar;
}
