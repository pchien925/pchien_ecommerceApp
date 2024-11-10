package com.PhamChien.ecommerce.mapper;

import com.PhamChien.ecommerce.domain.Product;
import com.PhamChien.ecommerce.dto.request.ProductRequestDTO;
import com.PhamChien.ecommerce.dto.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toProduct(ProductRequestDTO request);

    ProductResponse toProductResponse(Product product);

    void update(@MappingTarget Product product, ProductRequestDTO request);
}
