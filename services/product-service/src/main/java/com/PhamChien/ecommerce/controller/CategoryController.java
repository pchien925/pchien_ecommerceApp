package com.PhamChien.ecommerce.controller;

import com.PhamChien.ecommerce.dto.request.CategoryRequestDTO;
import com.PhamChien.ecommerce.dto.request.ProductRequestDTO;
import com.PhamChien.ecommerce.dto.response.ApiResponse;
import com.PhamChien.ecommerce.dto.response.CategoryResponse;
import com.PhamChien.ecommerce.dto.response.PageResponse;
import com.PhamChien.ecommerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ApiResponse<CategoryResponse> createCategory(@RequestBody CategoryRequestDTO requestDTO){
        return ApiResponse.<CategoryResponse>builder()
                .status(HttpStatus.CREATED.value())
                .data(categoryService.createCategory(requestDTO))
                .build();
    }

    @PutMapping("/{categoryId}")
    public ApiResponse<CategoryResponse> updateCategory(@PathVariable long categoryId,@RequestBody CategoryRequestDTO requestDTO){
        return ApiResponse.<CategoryResponse>builder()
                .status(HttpStatus.OK.value())
                .data(categoryService.update(categoryId, requestDTO))
                .build();
    }

    @DeleteMapping("/{categoryId}")
    public ApiResponse<String> deleteCategory(@PathVariable long categoryId){
        return ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .data(categoryService.delete(categoryId))
                .build();
    }

    @GetMapping("/{categoryId}")
    public ApiResponse<CategoryResponse> getDetailCategory(@PathVariable long categoryId){
        return ApiResponse.<CategoryResponse>builder()
                .status(HttpStatus.OK.value())
                .data(categoryService.getDetailCategory(categoryId))
                .build();
    }

    @GetMapping
    public ApiResponse<List<CategoryResponse>> getParentCategories(){
        return ApiResponse.<List<CategoryResponse>>builder()
                .status(HttpStatus.OK.value())
                .data(categoryService.getParentCategories())
                .build();
    }

    @GetMapping("/all")
    public ApiResponse<List<CategoryResponse>> getAllCategories(){
        return ApiResponse.<List<CategoryResponse>>builder()
                .status(HttpStatus.OK.value())
                .data(categoryService.getAllCategories())
                .build();
    }

    @GetMapping("/paging")
    public ApiResponse<PageResponse<CategoryResponse>> getPagingCategories(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size,
            @RequestParam(value = "sortBy", required = false, defaultValue = "createdAt") String sortBy
    ){
        return ApiResponse.<PageResponse<CategoryResponse>>builder()
                .status(HttpStatus.OK.value())
                .data(categoryService.getPagingCategories(page, size, sortBy))
                .build();
    }

    @GetMapping("/paging/all")
    public ApiResponse<PageResponse<CategoryResponse>> getPagingAllCategories(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size,
            @RequestParam(value = "sortBy", required = false, defaultValue = "createdAt") String sortBy
    ){
        return ApiResponse.<PageResponse<CategoryResponse>>builder()
                .status(HttpStatus.OK.value())
                .data(categoryService.getPagingAllCategories(page, size, sortBy))
                .build();
    }

    @GetMapping("/{categoryId}/all")
    public ApiResponse<List<CategoryResponse>> getSubCategories(@PathVariable long categoryId){
        return ApiResponse.<List<CategoryResponse>>builder()
                .status(HttpStatus.OK.value())
                .data(categoryService.getSubCategories(categoryId))
                .build();
    }

    @GetMapping("/{categoryId}/paging")
    public ApiResponse<PageResponse<CategoryResponse>> getPagingSubCategories(
            @PathVariable long categoryId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size,
            @RequestParam(value = "sortBy", required = false, defaultValue = "createdAt") String sortBy
    ){
        return ApiResponse.<PageResponse<CategoryResponse>>builder()
                .status(HttpStatus.OK.value())
                .data(categoryService.getPagingSubCategories(categoryId, page, size, sortBy))
                .build();
    }
}
