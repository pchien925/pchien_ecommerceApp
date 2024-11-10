package com.PhamChien.ecommerce.service.Impl;

import com.PhamChien.ecommerce.domain.Category;
import com.PhamChien.ecommerce.domain.ProductCategory;
import com.PhamChien.ecommerce.dto.request.CategoryRequestDTO;
import com.PhamChien.ecommerce.dto.response.CategoryResponse;
import com.PhamChien.ecommerce.dto.response.PageResponse;
import com.PhamChien.ecommerce.exception.InvalidDataException;
import com.PhamChien.ecommerce.exception.ResourceNotFoundException;
import com.PhamChien.ecommerce.mapper.CategoryMapper;
import com.PhamChien.ecommerce.repository.CategoryRepository;
import com.PhamChien.ecommerce.repository.ProductCategoryRepository;
import com.PhamChien.ecommerce.repository.ProductRepository;
import com.PhamChien.ecommerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    private final ProductCategoryRepository productCategoryRepository;
    private final ProductRepository productRepository;

    @Override
    public CategoryResponse createCategory(CategoryRequestDTO requestDTO){
        if(categoryRepository.existsByName(requestDTO.getName())){
            throw new InvalidDataException("Category name already exists");
        }
        Category category = categoryMapper.toCategory(requestDTO);
        Optional<Category> parentCategory = categoryRepository.findById(requestDTO.getParentCategoryId());

        category.setParentCategory(parentCategory.orElse(null));

        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse update(long categoryId, CategoryRequestDTO requestDTO) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        if(categoryRepository.existsByName(requestDTO.getName())){
            throw new InvalidDataException("Category name already exists");
        }
        categoryMapper.update(category, requestDTO);
        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Override
    public String delete(long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if(category.isEmpty())
            throw new ResourceNotFoundException("Category not found");
        categoryRepository.deleteById(categoryId);
        return "Category deleted";
    }

    @Override
    public CategoryResponse getDetailCategory(long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    public List<CategoryResponse> getParentCategories() {
        return categoryRepository.findByParentCategoryIsNull().stream().map(categoryMapper::toCategoryResponse).collect(Collectors.toList());
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream().map(categoryMapper::toCategoryResponse).toList();
    }

    @Override
    public PageResponse<CategoryResponse> getPagingCategories(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sortBy));

        Page<Category> categories = categoryRepository.findByParentCategoryIsNull(pageable);

        return PageResponse.<CategoryResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(categories.getTotalPages())
                .totalElements(categories.getTotalElements())
                .content(categories.getContent().stream().map(categoryMapper::toCategoryResponse).toList())
                .build();
    }

    @Override
    public PageResponse<CategoryResponse> getPagingAllCategories(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sortBy));

        Page<Category> categories = categoryRepository.findAll(pageable);

        return PageResponse.<CategoryResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(categories.getTotalPages())
                .totalElements(categories.getTotalElements())
                .content(categories.getContent().stream().map(categoryMapper::toCategoryResponse).toList())
                .build();
    }

    @Override
    public List<CategoryResponse> getSubCategories(long categoryId) {
        return categoryRepository.findByParentCategory_Id(categoryId).stream().map(categoryMapper::toCategoryResponse).collect(Collectors.toList());
    }

    @Override
    public PageResponse<CategoryResponse> getPagingSubCategories(long categoryId, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sortBy));

        Page<Category> categories = categoryRepository.findByParentCategory_Id(categoryId, pageable);

        return PageResponse.<CategoryResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(categories.getTotalPages())
                .totalElements(categories.getTotalElements())
                .content(categories.getContent().stream().map(categoryMapper::toCategoryResponse).toList())
                .build();
    }
}
