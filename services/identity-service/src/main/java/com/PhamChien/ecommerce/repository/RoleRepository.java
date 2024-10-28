package com.PhamChien.ecommerce.repository;

import com.PhamChien.ecommerce.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query(value = "select r from Role r inner join UserCredentialHasRole u on r.id = u.role.id where u.userCredential.id=:userCredentialId")
    List<Role> findAllByUserCredentialId(String userCredentialId);
    Optional<Role> findByName(String roleName);
}
