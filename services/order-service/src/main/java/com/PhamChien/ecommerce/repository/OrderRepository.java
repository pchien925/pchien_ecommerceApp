package com.PhamChien.ecommerce.repository;

import com.PhamChien.ecommerce.domain.Order;
import com.PhamChien.ecommerce.domain.OrderItem;
import com.PhamChien.ecommerce.domain.enumeration.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

    Optional<Order> findByUserIdAndStatusIn(String userId, Collection<OrderStatus> statuses);

    Page<Order> findByUserId(String userId, Pageable pageable);
}