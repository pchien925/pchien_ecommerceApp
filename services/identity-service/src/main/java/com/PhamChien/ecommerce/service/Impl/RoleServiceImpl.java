package com.PhamChien.ecommerce.service.Impl;

import com.PhamChien.ecommerce.domain.Role;
import com.PhamChien.ecommerce.domain.AccountHasRole;
import com.PhamChien.ecommerce.dto.request.AssignRoleRequest;
import com.PhamChien.ecommerce.exception.ResourceNotFoundException;
import com.PhamChien.ecommerce.repository.RoleRepository;
import com.PhamChien.ecommerce.repository.AccountHasRoleRepository;
import com.PhamChien.ecommerce.repository.AccountRepository;
import com.PhamChien.ecommerce.service.RoleService;
import com.PhamChien.ecommerce.util.RoleName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final AccountHasRoleRepository accountHasRoleRepository;
    private final RoleRepository roleRepository;
    private final AccountRepository accountRepository;

    @Override
    public String assignRole(AssignRoleRequest request) {
        if(accountHasRoleRepository.existsByRole_IdAndAccount_Id(request.getRoleId(), request.getAccountID())){
            return "user had this role";
        }
        AccountHasRole user = new AccountHasRole();
        user.setAccount(accountRepository.findById(request.getAccountID()).orElseThrow(() -> new ResourceNotFoundException("Account Not Found")));
        user.setRole(roleRepository.findById(request.getRoleId()).orElseThrow(() -> new ResourceNotFoundException("role not found")));
        accountHasRoleRepository.save(user);
        return "role assigned";
    }

    @Override
    public String revokeRole(AssignRoleRequest request) {

        AccountHasRole user =  accountHasRoleRepository.findByRole_IdAndAccount_Id(request.getRoleId(), request.getAccountID()).orElseThrow(() -> new ResourceNotFoundException("User not had this role"));
        accountHasRoleRepository.deleteById(user.getId());
        return "role revoked";
    }

    @Override
    public List<RoleName> getRoleNameList(String accountId){
        List<Role> roleList = roleRepository.findAllByAccountId(accountId);
        return roleList.stream().map(Role::getName).collect(Collectors.toList());
    }
}
