package com.PhamChien.ecommerce.service;

import com.PhamChien.ecommerce.dto.request.CategoryRequestDTO;
import com.PhamChien.ecommerce.dto.response.CategoryResponse;
import com.PhamChien.ecommerce.dto.response.PageResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequestDTO requestDTO);

    CategoryResponse update(long categoryId, CategoryRequestDTO requestDTO);

    String delete(long categoryId);

    CategoryResponse getDetailCategory(long categoryId);

    List<CategoryResponse> getParentCategories();

    List<CategoryResponse> getAllCategories();

    PageResponse<CategoryResponse> getPagingCategories(int page, int size, String sortBy);

    PageResponse<CategoryResponse> getPagingAllCategories(int page, int size, String sortBy);

    List<CategoryResponse> getSubCategories(long categoryId);

    PageResponse<CategoryResponse> getPagingSubCategories(long categoryId, int page, int size, String sortBy);
}
