package com.PhamChien.ecommerce.repository;

import com.PhamChien.ecommerce.domain.Role;
import com.PhamChien.ecommerce.domain.UserCredential;
import com.PhamChien.ecommerce.domain.UserCredentialHasRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCredentialHasRoleRepository extends JpaRepository<UserCredentialHasRole, Integer> {
    List<UserCredentialHasRole> findByUserCredential(UserCredential userCredential);

    List<UserCredentialHasRole> findByUserCredential_Id(String id);

    boolean existsByRole_IdAndUserCredential_Id(int roleId, String userCredentialId);

    Optional<UserCredentialHasRole> findByRole_IdAndUserCredential_Id(int roleId, String userCredentialId);

}
