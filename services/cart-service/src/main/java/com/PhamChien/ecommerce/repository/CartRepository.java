package com.PhamChien.ecommerce.repository;

import com.PhamChien.ecommerce.domain.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CartRepository extends MongoRepository<Cart, String> {
    Optional<Cart> findByUserId(String userId);

    Cart findByUserIdAndCartItems_ProductId(String userId, Long productId);


}
