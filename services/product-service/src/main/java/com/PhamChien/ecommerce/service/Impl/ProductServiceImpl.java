package com.PhamChien.ecommerce.service.Impl;

import com.PhamChien.ecommerce.client.MediaClient;
import com.PhamChien.ecommerce.domain.*;
import com.PhamChien.ecommerce.dto.request.InventoryRequestDTO;
import com.PhamChien.ecommerce.dto.request.PriceUpdateRequest;
import com.PhamChien.ecommerce.dto.request.ProductRequestDTO;
import com.PhamChien.ecommerce.dto.response.MediaResponse;
import com.PhamChien.ecommerce.dto.response.PageResponse;
import com.PhamChien.ecommerce.dto.response.ProductResponse;
import com.PhamChien.ecommerce.exception.ResourceNotFoundException;
import com.PhamChien.ecommerce.mapper.ProductMapper;
import com.PhamChien.ecommerce.repository.*;
import com.PhamChien.ecommerce.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductMediaRepository productMediaRepository;
    private final MediaClient mediaClient;

    @Override
    @Transactional
    public ProductResponse create(ProductRequestDTO request) {
        if (productRepository.existsByName(request.getName())){
            throw new ResourceNotFoundException("Product name already exists");
        }
        Product product = productMapper.toProduct(request);
        ProductResponse response;
        //create child product
        if (request.getParentProductId() != null) {
            Product parentProduct = productRepository.findById(request.getParentProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not existed"));
            product.setParentProduct(parentProduct);
            product.setBrand(parentProduct.getBrand());

            response = productMapper.toProductResponse(productRepository.save(product));

            productMediaRepository.saveAll(new HashSet<>(getProductMedias(request.getMediaIds(), product)));
            List<Long> categoryIds = parentProduct.getCategories().stream().map(
                    productCategory -> productCategory.getCategory().getId()
            ).toList();

            productCategoryRepository.saveAll(getProductCategories(categoryIds, product));
            addMediasToParentProduct(request.getMediaIds(), parentProduct);
        }
        //create parent product
        else{
            setParentProductAttributes(request, product);
            response = productMapper.toProductResponse(productRepository.save(product));

            productMediaRepository.saveAll(new HashSet<>(getProductMedias(request.getMediaIds(), product)));
            productCategoryRepository.saveAll(new HashSet<>(getProductCategories(request.getCategoryIds(), product)));
        }

        return response;
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(long productId, ProductRequestDTO request) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        productMapper.update(product, request);

        ProductResponse response;

        //nếu là child Prod
         if (product.getParentProduct() != null) {
             response = productMapper.toProductResponse(productRepository.save(product));
             updateProductMedias(request, product);
         }
         else {
             setParentProductAttributes(request, product);
             response = productMapper.toProductResponse(productRepository.save(product));
             updateProductCategories(request, product);
             productMediaRepository.saveAll(new HashSet<>(getProductMedias(request.getMediaIds(), product)));

             for (var childProduct : productRepository.findByParentProduct(product)) {
                 updateProductCategories(request, childProduct);
                 setProductBrand(childProduct.getParentProduct().getBrand().getId(), childProduct);
                 productRepository.save(product);
             }
         }

         return response;
    }

    @Override
    @Transactional
    public List<Long> deleteProduct(long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not existed"));

        List<Product> products = new ArrayList<>();
        products.add(product);
        if (product.getParentProduct() == null) {
            products.addAll(productRepository.findByParentProduct(product));
        }


        deleteProducts(products);
        return products.stream().map(Product::getId).toList();
    }

    @Override
    public ProductResponse getProduct(long productId) {
        return productMapper.toProductResponse(productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not existed")));
    }

    @Override
    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream().map(productMapper::toProductResponse).toList();
    }

    @Override
    public List<ProductResponse> findAllParentProduct() {
        return productRepository.findByParentProductIsNull().stream().map(productMapper::toProductResponse).toList();
    }

    @Override
    public PageResponse<ProductResponse> getPagingAllProducts(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sortBy));

        Page<Product> products = productRepository.findAll(pageable);
        return PageResponse.<ProductResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(products.getTotalPages())
                .totalElements(products.getTotalElements())
                .content(products.getContent().stream().map(productMapper::toProductResponse).toList())
                .build();
    }

    @Override
    public PageResponse<ProductResponse> getPagingProducts(int page, int size, String name, String brandName, String sortField, String sortOrder) {
        Sort.Direction direction = Sort.Direction.fromString(sortOrder);
        Sort sort = Sort.by(direction, sortField);

        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<Product> products = productRepository.searchParentProductsByNameAndBrand(name, brandName, pageable);
        return PageResponse.<ProductResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(products.getTotalPages())
                .totalElements(products.getTotalElements())
                .content(products.getContent().stream().map(productMapper::toProductResponse).toList())
                .build();
    }

    @Override
    public List<ProductResponse> getSubProducts(long productId) {
        return productRepository.findByParentProduct_Id(productId).stream().map(productMapper::toProductResponse).toList();
    }

    @Override
    public PageResponse<ProductResponse> getProductsByBrand(int brandId, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sortBy));

        Page<Product> products = productRepository.findByBrand_IdAndParentProductIsNull(brandId, pageable);
        return PageResponse.<ProductResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(products.getTotalPages())
                .totalElements(products.getTotalElements())
                .content(products.getContent().stream().map(productMapper::toProductResponse).toList())
                .build();
    }

    @Override
    public PageResponse<ProductResponse> getProductsByCategory(long categoryId, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sortBy));

        Page<Product> products = productRepository.findByCategory_IdAndParentProductIsNull(categoryId, pageable);
        return PageResponse.<ProductResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(products.getTotalPages())
                .totalElements(products.getTotalElements())
                .content(products.getContent().stream().map(productMapper::toProductResponse).toList())
                .build();
    }

    @Override
    public List<MediaResponse> getMediasByProduct(long productId) {
        return mediaClient.getAllMedia(this.getMediaIdsByProduct(productId)).getData();
    }

    @Override
    public String updateIsActive(long productId, boolean isActive) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not existed"));
        productRepository.save(product);
        return "Product'activation updated";
    }

    @Override
    public String updatePrice(long productId, PriceUpdateRequest request) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not existed"));
        if (request.getPrice() != null)
            product.setPrice(request.getPrice());
        if (request.getPromotionalPrice() != null)
            product.setPromotionalPrice(request.getPromotionalPrice());
        productRepository.save(product);
        return "Product price updated";
    }

    @Override
    public String updateThumbnailUrl(long productId, long mediaId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not existed"));
        product.setThumbnailUrl(mediaClient.getMedia(mediaId).getData().getUrl());
        productRepository.save(product);
        return "Product thumbnail updated with url: " + product.getThumbnailUrl();
    }

    @Override
    public PageResponse<ProductResponse>getProductsByCategoryIds(int page, int size, String sortField, String sortOrder, List<Long> categoryIds) {
        Sort.Direction direction = Sort.Direction.fromString(sortOrder);
        Sort sort = Sort.by(direction, sortField);

        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Product> products = productRepository.findProductsByCategoryIds(categoryIds, pageable);
        return PageResponse.<ProductResponse>builder()
                .currentPage(1)
                .pageSize(5)
                .totalPages(products.getTotalPages())
                .totalElements(products.getTotalElements())
                .content(products.getContent().stream().map(productMapper::toProductResponse).toList())
                .build();
    }

    @Override
    public String updateSoldQuantity(long productId, int quantity) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not existed"));
        product.setSold(product.getSold() + quantity);
        productRepository.save(product);
        return "Product sold quantity updated";
    }

    private List<Long> getMediaIdsByProduct(long productId) {
        if (productRepository.findById(productId).isEmpty()) {
            throw new ResourceNotFoundException("Product not existed");
        }

        List<ProductMedia> list = productMediaRepository.findByProduct_Id(productId);

        List<Long> mediaIds = new ArrayList<>();

        for (ProductMedia productMedia : list) {
            mediaIds.add(productMedia.getMediaId());
        }
        return mediaIds;
    }

    private List<ProductCategory> getProductCategories(List<Long> categoryIds, Product product) {
        List<ProductCategory> productCategories = new ArrayList<>();
        if (CollectionUtils.isEmpty(categoryIds)) {
            productCategoryRepository.deleteByProduct(product);
            return productCategories;
        }

        List<Category> categories = categoryRepository.findAllById(categoryIds);

        for (Category category : categories) {
            ProductCategory productCategory = ProductCategory.builder()
                    .category(category)
                    .product(product)
                    .build();
            if (!productCategoryRepository.existsByProductAndCategory(product, category))
                productCategories.add(productCategory);
        }
        return productCategories;
    }

    private List<ProductMedia> getProductMedias(List<Long> mediaIds, Product product) {
        List<ProductMedia> productMedias = new ArrayList<>();
        if (CollectionUtils.isEmpty(mediaIds) && product.getParentProduct() == null) {
            productMediaRepository.deleteByProduct(product);
            return productMedias;
        }
        for (Long mediaId : mediaIds) {
            ProductMedia productMedia = ProductMedia.builder()
                    .product(product)
                    .mediaId(mediaId)
                    .build();
            if (!productMediaRepository.existsByMediaIdAndProduct(mediaId, product))
                productMedias.add(productMedia);
        }
        return productMedias;
    }

    private void addMediasToParentProduct(List<Long> mediaIds, Product product)
    {
        if (CollectionUtils.isEmpty(mediaIds)) {
            return;
        }
        List<ProductMedia> productMedias = new ArrayList<>();
        for (Long mediaId : mediaIds) {
            ProductMedia productMedia = ProductMedia.builder()
                    .product(product)
                    .mediaId(mediaId)
                    .build();
            if (!productMediaRepository.existsByMediaIdAndProduct(mediaId, product))
                productMedias.add(productMedia);
        }
        productMediaRepository.saveAll(productMedias);
    }

    private void setProductBrand(Integer brandId, Product product) {
        if (brandId != null && (product.getBrand() == null || !(brandId.equals(product.getBrand().getId())))){
            Brand brand = brandRepository.findById(brandId)
                    .orElseThrow(() -> new ResourceNotFoundException("Brand not existed"));
            product.setBrand(brand);
        }
    }

    private void updateProductMedias(ProductRequestDTO requestDTO, Product product){
        if (CollectionUtils.isEmpty(requestDTO.getMediaIds())) {
            return;
        }

        List<Long> oldMedias = product.getMedias().stream().map(ProductMedia::getMediaId).toList();

        List<Long> addMedias =  requestDTO.getMediaIds().stream()
                .filter(element -> !oldMedias.contains(element))
                .toList();
        List<Long> deleteMedias = oldMedias.stream()
                .filter(element -> !requestDTO.getMediaIds().contains(element))
                .toList();
        if (!CollectionUtils.isEmpty(deleteMedias)) {
                productMediaRepository.deleteByMediaIdIn(deleteMedias);
        }

        if (!CollectionUtils.isEmpty(addMedias)) {
        productMediaRepository.saveAll(getProductMedias(addMedias, product));
        productMediaRepository.saveAll(getProductMedias(addMedias, product.getParentProduct()));
        }
    }


    private void updateProductCategories(ProductRequestDTO requestDTO, Product product){
        if(CollectionUtils.isEmpty(requestDTO.getCategoryIds())) {
            return;
        }
        List<Category> oldCategories = productCategoryRepository.findByProduct(product).stream()
                .map(ProductCategory::getCategory)
                .toList();
        List<Category> newCategories = categoryRepository.findAllById(requestDTO.getCategoryIds());

        List<Category> addCategories =  newCategories.stream()
                .filter(element -> !oldCategories.contains(element))
                .toList();

        List<Category> deleteCategories =  oldCategories.stream()
                .filter(element -> !newCategories.contains(element))
                .toList();

        if (!CollectionUtils.isEmpty(deleteCategories)) {
            productCategoryRepository.deleteByCategoryIn(deleteCategories);
        }
        if(!CollectionUtils.isEmpty(addCategories)) {
            productCategoryRepository.saveAll(getProductCategories(addCategories.stream().map(Category::getId).toList(), product));
        }
    }

    private void setParentProductAttributes(ProductRequestDTO request, Product product) {
        product.setColor(null);
        product.setSize(null);
        setProductBrand(request.getBrandId(), product);
    }

    private void deleteProducts(List<Product> products){
        productCategoryRepository.deleteByProductIn(products);
        productMediaRepository.deleteByProductIn(products);
        productRepository.deleteAll(products);
    }
}
