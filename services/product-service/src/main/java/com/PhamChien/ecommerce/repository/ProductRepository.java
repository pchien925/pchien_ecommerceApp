package com.PhamChien.ecommerce.repository;

import com.PhamChien.ecommerce.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
    List<Product> findByParentProductIsNull();
    Page<Product> findByParentProductIsNull(Pageable pageable);

    Page<Product> findByBrand_IdAndParentProductIsNull(int id, Pageable pageable);

    List<Product> findByParentProduct_Id(Long id);

    @Query(value = "select p from Product p inner join ProductCategory pc on p.id = pc.product.id where pc.category.id=:categoryId")
    Page<Product> findByCategory_IdAndParentProductIsNull(Long categoryId, Pageable pageable);

    @Query("select p from Product p where p.parentProduct = ?1")

    List<Product> findByParentProduct(Product parentProduct);

    boolean existsByName(String name);
}
