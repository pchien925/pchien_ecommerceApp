package com.PhamChien.ecommerce.repository;

import com.PhamChien.ecommerce.domain.Account;
import com.PhamChien.ecommerce.domain.AccountHasRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountHasRoleRepository extends JpaRepository<AccountHasRole, Integer> {
    List<AccountHasRole> findByAccount(Account account);

    List<AccountHasRole> findByAccount_Id(String accountId);

    boolean existsByRole_IdAndAccount_Id(int roleId, String accountId);

    Optional<AccountHasRole> findByRole_IdAndAccount_Id(int roleId, String accountId);

}
