package com.PhamChien.ecommerce.service.Impl;

import com.PhamChien.ecommerce.domain.Brand;
import com.PhamChien.ecommerce.domain.Product;
import com.PhamChien.ecommerce.dto.request.BrandRequestDTO;
import com.PhamChien.ecommerce.dto.response.BrandResponse;
import com.PhamChien.ecommerce.dto.response.PageResponse;
import com.PhamChien.ecommerce.dto.response.ProductResponse;
import com.PhamChien.ecommerce.exception.InvalidDataException;
import com.PhamChien.ecommerce.exception.ResourceNotFoundException;
import com.PhamChien.ecommerce.mapper.BrandMapper;
import com.PhamChien.ecommerce.mapper.ProductMapper;
import com.PhamChien.ecommerce.repository.BrandRepository;
import com.PhamChien.ecommerce.repository.ProductRepository;
import com.PhamChien.ecommerce.service.BrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrandServiceImpl implements BrandService {
    private final BrandMapper brandMapper;
    private final ProductMapper productMapper;

    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;

    @Override
    public List<BrandResponse> getAllBrands(){
        return brandRepository.findAll().stream().map(brandMapper::toBrandResponse).toList();
    }

    @Override
    public PageResponse<BrandResponse> getAllBrands(int page, int size, String sortBy){
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sortBy));

        Page<Brand> brands = brandRepository.findAll(pageable);
        return PageResponse.<BrandResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(brands.getTotalPages())
                .totalElements(brands.getTotalElements())
                .content(brands.getContent().stream().map(brandMapper::toBrandResponse).toList())
                .build();
    }

    @Override
    public BrandResponse createBrand(BrandRequestDTO requestDTO){
        if(brandRepository.existsByName(requestDTO.getName()))
            throw new InvalidDataException("brand name already exists");
        Brand brand = brandMapper.toBrand(requestDTO);
        return brandMapper.toBrandResponse(brandRepository.save(brand));
    }

    @Override
    public BrandResponse updateBrand(int brandId, BrandRequestDTO requestDTO) {
        Brand brand = brandRepository.findById(brandId).orElseThrow(() -> new ResourceNotFoundException("brand not found"));
        if(brandRepository.existsByName(requestDTO.getName()))
            throw new InvalidDataException("brand name already exists");
        brandMapper.update(brand, requestDTO);
        return brandMapper.toBrandResponse(brand);
    }

    @Override
    public String deleteBrand(int brandId) {
        Optional<Brand> brand = brandRepository.findById(brandId);
        if(brand.isEmpty())
            throw new ResourceNotFoundException("brand not found");
        brandRepository.deleteById(brandId);
        return "Deleted";
    }

    @Override
    public BrandResponse getBrand(int brandId) {
        return brandMapper.toBrandResponse(brandRepository.findById(brandId).orElseThrow(() -> new ResourceNotFoundException("brand not found")));
    }
}
