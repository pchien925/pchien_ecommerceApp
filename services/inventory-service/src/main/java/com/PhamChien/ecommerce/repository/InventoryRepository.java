package com.PhamChien.ecommerce.repository;

import com.PhamChien.ecommerce.domain.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    boolean existsByProductId(String productId);

    Inventory findByProductId(String productId);
}
