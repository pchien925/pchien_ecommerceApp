package com.PhamChien.ecommerce.repository;

import com.PhamChien.ecommerce.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByUsername(String username);
    Optional<Account> findByVerificationCode(String verificationCode);
    Optional<Account> findByEmail(String email);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
}
