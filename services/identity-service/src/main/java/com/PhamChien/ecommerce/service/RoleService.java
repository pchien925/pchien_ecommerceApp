package com.PhamChien.ecommerce.service;

import com.PhamChien.ecommerce.dto.request.AssignRoleRequest;
import com.PhamChien.ecommerce.dto.response.UserCredentialResponse;
import com.PhamChien.ecommerce.util.RoleName;

import java.util.List;

public interface RoleService {
    String assignRole(AssignRoleRequest request);

    String revokeRole(AssignRoleRequest request);

    List<RoleName> getRoleNameList(String userCreadentialId);
}
