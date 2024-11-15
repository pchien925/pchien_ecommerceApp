package com.PhamChien.ecommerce.controller;

import com.PhamChien.ecommerce.dto.request.PriceUpdateRequest;
import com.PhamChien.ecommerce.dto.request.ProductRequestDTO;
import com.PhamChien.ecommerce.dto.response.ApiResponse;
import com.PhamChien.ecommerce.dto.response.MediaResponse;
import com.PhamChien.ecommerce.dto.response.PageResponse;
import com.PhamChien.ecommerce.dto.response.ProductResponse;
import com.PhamChien.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ApiResponse<ProductResponse> addProduct(@RequestBody ProductRequestDTO request) {
        return ApiResponse.<ProductResponse>builder()
                .status(HttpStatus.CREATED.value())
                .data(productService.create(request))
                .build();
    }

    @PutMapping("/{productId}")
    public ApiResponse<ProductResponse> updateProduct(@PathVariable("productId") long productId, @RequestBody ProductRequestDTO request){
        return ApiResponse.<ProductResponse>builder()
                .status(HttpStatus.OK.value())
                .data(productService.updateProduct(productId, request))
                .build();
    }

    @PatchMapping("/{productId}/status")
    public ApiResponse<String> updateIsActive(@PathVariable("productId") long productId, @RequestParam boolean isActive){
        return ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .data(productService.updateIsActive(productId, isActive))
                .build();
    }

    @PatchMapping("/{productId}/price")
    public ApiResponse<String> updatePrice(@PathVariable("productId") long productId, @RequestBody PriceUpdateRequest request){
        return ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .data(productService.updatePrice(productId, request))
                .build();
    }

    @PatchMapping("/{productId}/thumbnailUrl")
    public ApiResponse<String> updateThumbnailUrl(@PathVariable("productId") long productId, @RequestParam Long mediaId){
        return ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .data(productService.updateThumbnailUrl(productId, mediaId))
                .build();
    }

    @DeleteMapping("/{productId}")
    public ApiResponse<String> deleteProduct(@PathVariable("productId") long productId){
        return ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .data(productService.deleteProduct(productId))
                .build();
    }

    @GetMapping("/{productId}")
    public ApiResponse<ProductResponse> getProduct(@PathVariable long productId) {
        return ApiResponse.<ProductResponse>builder()
                .status(HttpStatus.OK.value())
                .data(productService.getProduct(productId))
                .build();
    }

    @GetMapping("/all")
    public ApiResponse<List<ProductResponse>> getAllProducts() {
        return ApiResponse.<List<ProductResponse>>builder()
                .status(HttpStatus.OK.value())
                .data(productService.findAll())
                .build();
    }

    @GetMapping
    public ApiResponse<List<ProductResponse>> getAllParentProducts() {
        return ApiResponse.<List<ProductResponse>>builder()
                .status(HttpStatus.OK.value())
                .data(productService.findAllParentProduct())
                .build();
    }

    @GetMapping("/paging/all")
    public ApiResponse<PageResponse<ProductResponse>> getPagingAllProducts(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size,
            @RequestParam(value = "sortBy", required = false, defaultValue = "createdAt") String sortBy
            ){
        return ApiResponse.<PageResponse<ProductResponse>>builder()
                .status(HttpStatus.OK.value())
                .data(productService.getPagingAllProducts(page, size, sortBy))
                .build();
    }

    @GetMapping("/paging")
    public ApiResponse<PageResponse<ProductResponse>> getPagingProducts(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size,
            @RequestParam(value = "sortBy", required = false, defaultValue = "createdAt") String sortBy
    ){
        return ApiResponse.<PageResponse<ProductResponse>>builder()
                .status(HttpStatus.OK.value())
                .data(productService.getPagingProducts(page, size, sortBy))
                .build();
    }

    @GetMapping("/{productId}/all")
    public ApiResponse<List<ProductResponse>> getSubProducts(@PathVariable long productId) {
        return ApiResponse.<List<ProductResponse>>builder()
                .status(HttpStatus.OK.value())
                .data(productService.getSubProducts(productId))
                .build();
    }

    @GetMapping("/brands/{brandId}/paging")
    public ApiResponse<PageResponse<ProductResponse>> getProductsByBrand(
            @PathVariable("brandId") int brandId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size,
            @RequestParam(value = "sortBy", required = false, defaultValue = "createdAt") String sortBy
    ){
        return ApiResponse.<PageResponse<ProductResponse>>builder()
                .status(HttpStatus.OK.value())
                .data(productService.getProductsByBrand(brandId, page, size, sortBy))
                .build();
    }

    @GetMapping("/categories/{categoryId}/paging")
    public ApiResponse<PageResponse<ProductResponse>> getProductsByCategory(
            @PathVariable("categoryId") long categoryId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size,
            @RequestParam(value = "sortBy", required = false, defaultValue = "createdAt") String sortBy
    ){
        return ApiResponse.<PageResponse<ProductResponse>>builder()
                .status(HttpStatus.OK.value())
                .data(productService.getProductsByCategory(categoryId, page, size, sortBy))
                .build();
    }

    @GetMapping("/medias/{productId}")
    public ApiResponse<List<MediaResponse>> getMediasByProduct(@PathVariable("productId") long productId) {
        return ApiResponse.<List<MediaResponse>>builder()
                .status(HttpStatus.OK.value())
                .data(productService.getMediasByProduct(productId))
                .build();
    }
}
