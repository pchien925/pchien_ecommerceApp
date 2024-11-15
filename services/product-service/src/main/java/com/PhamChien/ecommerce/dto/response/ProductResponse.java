package com.PhamChien.ecommerce.dto.response;

import com.PhamChien.ecommerce.domain.Brand;
import com.PhamChien.ecommerce.domain.Product;
import com.PhamChien.ecommerce.domain.ProductMedia;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
public class ProductResponse {
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

    private Product parentProduct;

    private Brand brand;
}
