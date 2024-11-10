package com.PhamChien.ecommerce.mapper;

import com.PhamChien.ecommerce.domain.Category;
import com.PhamChien.ecommerce.dto.request.CategoryRequestDTO;
import com.PhamChien.ecommerce.dto.response.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(CategoryRequestDTO requestDTO);
    CategoryResponse toCategoryResponse(Category category);

    void update(@MappingTarget Category category, CategoryRequestDTO requestDTO);

}
