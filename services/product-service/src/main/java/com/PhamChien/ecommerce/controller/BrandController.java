package com.PhamChien.ecommerce.controller;

import com.PhamChien.ecommerce.dto.request.BrandRequestDTO;
import com.PhamChien.ecommerce.dto.response.ApiResponse;
import com.PhamChien.ecommerce.dto.response.BrandResponse;
import com.PhamChien.ecommerce.dto.response.PageResponse;
import com.PhamChien.ecommerce.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/brands")
public class BrandController {
    private final BrandService brandService;

    @GetMapping
    public ApiResponse<List<BrandResponse>> getAll() {
        return ApiResponse.<List<BrandResponse>>builder()
                .status(HttpStatus.OK.value())
                .data(brandService.getAllBrands())
                .build();
    }

    @GetMapping("/paging")
    public ApiResponse<PageResponse<BrandResponse>> getBrandsPaging(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size,
            @RequestParam(value = "sortBy", required = false, defaultValue = "createdAt") String sortBy) {
        return ApiResponse.<PageResponse<BrandResponse>>builder()
                .status(HttpStatus.OK.value())
                .data(brandService.getAllBrands(page, size, sortBy))
                .build();
    }

    @PostMapping
    public ApiResponse<BrandResponse> createBrand(@RequestBody BrandRequestDTO requestDTO){
        return ApiResponse.<BrandResponse>builder()
                .status(HttpStatus.CREATED.value())
                .data(brandService.createBrand(requestDTO))
                .build();
    }

    @PutMapping("/{brandId}")
    public ApiResponse<BrandResponse> updateBrand(@PathVariable("brandId") int brandId, @RequestBody BrandRequestDTO requestDTO){
        return ApiResponse.<BrandResponse>builder()
                .status(HttpStatus.OK.value())
                .data(brandService.updateBrand(brandId, requestDTO))
                .build();
    }

    @DeleteMapping("/{brandId}")
    public ApiResponse<String> deleteBrand(@PathVariable("brandId") int brandId){
        return ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .data(brandService.deleteBrand(brandId))
                .build();
    }

    @GetMapping("/{brandId}")
    public ApiResponse<BrandResponse> getBrand(@PathVariable("brandId") int brandId){
        return ApiResponse.<BrandResponse>builder()
                .status(HttpStatus.OK.value())
                .data(brandService.getBrand(brandId))
                .build();
    }
}
