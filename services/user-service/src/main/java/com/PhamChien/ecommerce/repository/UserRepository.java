package com.PhamChien.ecommerce.repository;

import com.PhamChien.ecommerce.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByCredentialId(String credentialId);

    @Query(value = "SELECT u FROM User u " +
            "WHERE (:fullName IS NULL OR u.fullName LIKE CONCAT('%', :fullName, '%')) " +
            "AND (:phone IS NULL OR u.phone LIKE CONCAT('%', :phone, '%')) " +
            "AND (:credentialId IS NULL OR u.credentialId LIKE CONCAT('%', :credentialId, '%'))")
    Page<User> searchUsers(
            @Param("fullName") String fullName,
            @Param("phone") String phone,
            @Param("credentialId") String credentialId,
            Pageable pageable);

}
