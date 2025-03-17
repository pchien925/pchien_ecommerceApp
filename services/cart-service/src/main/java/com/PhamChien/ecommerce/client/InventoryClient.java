package com.PhamChien.ecommerce.client;


import com.PhamChien.ecommerce.dto.response.ApiResponse;
import com.PhamChien.ecommerce.dto.response.InventoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;

@FeignClient(name = "inventory-service", url = "http://localhost:8087/api/v1/inventory")
public interface InventoryClient {
    @GetMapping("/{productId}")
    ApiResponse<InventoryResponse> getInventory(@PathVariable("productId") Long productId);
}
