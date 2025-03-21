package com.PhamChien.ecommerce.service;

import com.PhamChien.ecommerce.dto.request.PriceUpdateRequest;
import com.PhamChien.ecommerce.dto.request.ProductRequestDTO;
import com.PhamChien.ecommerce.dto.response.MediaResponse;
import com.PhamChien.ecommerce.dto.response.PageResponse;
import com.PhamChien.ecommerce.dto.response.ProductDetailResponse;
import com.PhamChien.ecommerce.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse create(ProductRequestDTO request);

    List<ProductResponse> findAll();

    ProductResponse updateProduct(long productId, ProductRequestDTO request);

    ProductResponse getProduct(long productId);

    public List<Long> deleteProduct(long productId);

    List<ProductResponse> findAllParentProduct();

    PageResponse<ProductResponse> getPagingAllProducts(int page, int size, String sortBy);

    PageResponse<ProductResponse> getPagingProducts(int page, int size, String name, String brandName, String sortField, String sortOrder);

    List<ProductResponse> getSubProducts(long productId);

    PageResponse<ProductResponse> getProductsByBrand(int brandId, int page, int size, String sortBy);

    PageResponse<ProductResponse> getProductsByCategory(long categoryId, int page, int size, String sortBy);

    List<MediaResponse> getMediasByProduct(long productId);

    String updateIsActive(long productId, boolean isActive);

    String updatePrice(long productId, PriceUpdateRequest request);


    String updateThumbnailUrl(long productId, long mediaId);

    PageResponse<ProductResponse> getProductsByCategoryIds(int page, int size, String sortField, String sortOrder, List<Long> categoryIds);

    String updateSoldQuantity(long productId, int quantity);
}
