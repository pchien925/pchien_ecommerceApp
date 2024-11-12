package com.PhamChien.ecommerce.dto.response;

import com.PhamChien.ecommerce.domain.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Builder
@Setter
@Getter
public class IntrospectResponse {
    private boolean isValid;
    private String accountId;
    private String username;
    private List<String> roleName;
    private Date expiresAt;
}
