package com.PhamChien.ecommerce.client;

import com.PhamChien.ecommerce.dto.response.ApiResponse;
import com.PhamChien.ecommerce.dto.response.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", url = "http://localhost:8085/api/v1/products")
public interface ProductClient {
    @GetMapping(value = "/{productId}")
    public ApiResponse<ProductResponse> getProduct(@PathVariable long productId);
}
