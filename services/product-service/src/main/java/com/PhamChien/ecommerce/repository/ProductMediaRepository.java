package com.PhamChien.ecommerce.repository;

import com.PhamChien.ecommerce.domain.Product;
import com.PhamChien.ecommerce.domain.ProductMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductMediaRepository extends JpaRepository<ProductMedia, Long> {
    void deleteByProduct(Product product);

    List<ProductMedia> findByProduct_Id(Long productId);

    Optional<ProductMedia> findByProductAndMediaId(Product product, Long mediaId);

    boolean existsByMediaIdAndProduct(Long mediaId, Product product);

    void deleteByMediaId(Long mediaId);

    void deleteByProductIn(Collection<Product> products);

    void deleteByMediaIdIn(Collection<Long> mediaIds);

}
