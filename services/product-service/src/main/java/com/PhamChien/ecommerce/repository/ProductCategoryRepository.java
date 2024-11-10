package com.PhamChien.ecommerce.repository;

import com.PhamChien.ecommerce.domain.Category;
import com.PhamChien.ecommerce.domain.Product;
import com.PhamChien.ecommerce.domain.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

    boolean existsByProductAndCategory(Product product, Category category);

    void deleteByProduct(Product product);

    List<ProductCategory> findByProduct(Product product);

    List<ProductCategory> findByCategory_Id(Long id);

    void deleteByProductIn(Collection<Product> products);

    void deleteByCategoryIn(Collection<Category> categories);

}
