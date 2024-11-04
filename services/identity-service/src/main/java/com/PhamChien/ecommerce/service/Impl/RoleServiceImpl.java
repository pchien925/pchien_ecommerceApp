package com.PhamChien.ecommerce.service.Impl;

import com.PhamChien.ecommerce.domain.Role;
import com.PhamChien.ecommerce.domain.UserCredentialHasRole;
import com.PhamChien.ecommerce.dto.request.AssignRoleRequest;
import com.PhamChien.ecommerce.exception.ResourceNotFoundException;
import com.PhamChien.ecommerce.repository.RoleRepository;
import com.PhamChien.ecommerce.repository.UserCredentialHasRoleRepository;
import com.PhamChien.ecommerce.repository.UserCredentialRepository;
import com.PhamChien.ecommerce.service.RoleService;
import com.PhamChien.ecommerce.util.RoleName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final UserCredentialHasRoleRepository userCredentialHasRoleRepository;
    private final RoleRepository roleRepository;
    private final UserCredentialRepository userCredentialRepository;

    @Override
    public String assignRole(AssignRoleRequest request) {
        if(userCredentialHasRoleRepository.existsByRole_IdAndUserCredential_Id(request.getRoleId(), request.getUserCredentialId())){
            return "user had this role";
        }
        UserCredentialHasRole user = new UserCredentialHasRole();
        user.setUserCredential(userCredentialRepository.findById(request.getUserCredentialId()).orElseThrow(() -> new ResourceNotFoundException("User Credential Not Found")));
        user.setRole(roleRepository.findById(request.getRoleId()).orElseThrow(() -> new ResourceNotFoundException("role not found")));
        userCredentialHasRoleRepository.save(user);
        return "role assigned";
    }

    @Override
    public String revokeRole(AssignRoleRequest request) {

        UserCredentialHasRole user =  userCredentialHasRoleRepository.findByRole_IdAndUserCredential_Id(request.getRoleId(), request.getUserCredentialId()).orElseThrow(() -> new ResourceNotFoundException("User not had this role"));
        userCredentialHasRoleRepository.deleteById(user.getId());
        return "role revoked";
    }

    @Override
    public List<RoleName> getRoleNameList(String userCreadentialId){
        List<Role> roleList = roleRepository.findAllByUserCredentialId(userCreadentialId);
        return roleList.stream().map(Role::getName).collect(Collectors.toList());
    }
}
