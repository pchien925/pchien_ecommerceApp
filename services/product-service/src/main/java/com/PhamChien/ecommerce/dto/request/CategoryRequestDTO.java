package com.PhamChien.ecommerce.dto.request;

import lombok.Getter;

@Getter
public class CategoryRequestDTO {
    private String name;

    private String thumbnailUrl;

    private long parentCategoryId;
}
