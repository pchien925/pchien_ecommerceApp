package com.PhamChien.ecommerce.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductDetailResponse {
    private Long id;

    private String name;

    private String description;

    private String slug;

    private Double price;

    private Double promotionalPrice;

    private String size;

    private String color;

    private Integer sold;

    private boolean isActive;

    private String thumbnailUrl;

}
