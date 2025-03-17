package com.PhamChien.ecommerce.repository;

import com.PhamChien.ecommerce.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
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

    @Query(value = "SELECT p FROM Product p " +
            "LEFT JOIN p.brand b " +
            "WHERE (:name IS NULL OR p.name LIKE CONCAT('%', :name, '%')) " +
            "AND (:brandName IS NULL OR b.name LIKE CONCAT('%', :brandName, '%')) " +
            "AND p.parentProduct IS NULL")
    Page<Product> searchParentProductsByNameAndBrand(
            @Param("name") String name,
            @Param("brandName") String brandName,
            Pageable pageable);

    @Query("SELECT p FROM Product p " +
            "JOIN p.categories pc " +
            "JOIN pc.category c " +
            "WHERE c.id IN :categoryIds")
    Page<Product> findProductsByCategoryIds(List<Long> categoryIds, Pageable pageable);
}
