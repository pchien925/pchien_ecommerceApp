package com.PhamChien.ecommerce.mapper;

import com.PhamChien.ecommerce.domain.Brand;
import com.PhamChien.ecommerce.dto.request.BrandRequestDTO;
import com.PhamChien.ecommerce.dto.response.BrandResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BrandMapper {
    BrandResponse toBrandResponse(Brand brand);

    Brand toBrand(BrandRequestDTO requestDTO);

    void update(@MappingTarget Brand brand, BrandRequestDTO requestDTO);
}
