package com.PhamChien.ecommerce.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class ProductRequestDTO {
    private String name;

    private String description;

    private String slug;

    private Double price;

    private Double promotionalPrice;

    private String size;

    private String color;

    private long quantity;

    private int sold;


    private String thumbnailUrl;

    private List<Long> mediaIds;


    private Long parentProductId;

    private Integer brandId;

    private List<Long> categoryIds;
}
