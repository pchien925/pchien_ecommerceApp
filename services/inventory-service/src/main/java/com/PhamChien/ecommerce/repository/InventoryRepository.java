package com.PhamChien.ecommerce.repository;

import com.PhamChien.ecommerce.domain.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    boolean existsByProductId(Long productId);

    Optional<Inventory> findByProductId(Long productId);

    void deleteByProductIdIn(Collection<Long> productIds);
}
