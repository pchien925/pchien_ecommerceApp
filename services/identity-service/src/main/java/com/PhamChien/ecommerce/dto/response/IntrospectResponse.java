package com.PhamChien.ecommerce.dto.response;

import com.PhamChien.ecommerce.domain.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Builder
@Setter
@Getter
public class IntrospectResponse {
    private boolean isValid;
    private String userId;
    private Set<Role> role;
}
