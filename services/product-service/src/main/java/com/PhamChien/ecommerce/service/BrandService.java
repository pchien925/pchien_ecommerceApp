package com.PhamChien.ecommerce.service;

import com.PhamChien.ecommerce.dto.request.BrandRequestDTO;
import com.PhamChien.ecommerce.dto.response.BrandResponse;
import com.PhamChien.ecommerce.dto.response.PageResponse;
import com.PhamChien.ecommerce.dto.response.ProductResponse;

import java.util.List;

public interface BrandService {
    List<BrandResponse> getAllBrands();

    PageResponse<BrandResponse> getAllBrands(int page, int size, String sortBy);

    BrandResponse createBrand(BrandRequestDTO requestDTO);

    BrandResponse updateBrand(int brandId, BrandRequestDTO requestDTO);

    String deleteBrand(int brandId);

    BrandResponse getBrand(int brandId);
}
