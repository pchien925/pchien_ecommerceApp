package com.PhamChien.ecommerce.repository;

import com.PhamChien.ecommerce.domain.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCredentialRepository extends JpaRepository<UserCredential, String> {
    Optional<UserCredential> findByUsername(String username);
    Optional<UserCredential> findByVerificationCode(String verificationCode);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
}
