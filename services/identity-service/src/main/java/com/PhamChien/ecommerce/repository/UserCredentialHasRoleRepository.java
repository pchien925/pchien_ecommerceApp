package com.PhamChien.ecommerce.repository;

import com.PhamChien.ecommerce.domain.UserCredentialHasRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCredentialHasRoleRepository extends JpaRepository<UserCredentialHasRole, Integer> {
}
