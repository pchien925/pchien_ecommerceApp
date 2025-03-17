package com.PhamChien.ecommerce.repository;

import com.PhamChien.ecommerce.domain.Category;
import com.PhamChien.ecommerce.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{

    boolean existsByName(String name);
}
