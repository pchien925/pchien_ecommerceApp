package com.PhamChien.ecommerce.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AssignRoleRequest {
    Integer roleId;
    String accountId;
}
