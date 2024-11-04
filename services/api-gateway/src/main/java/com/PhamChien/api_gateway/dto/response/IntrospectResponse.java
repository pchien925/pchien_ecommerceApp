package com.PhamChien.api_gateway.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Builder
@Setter
@Getter
public class IntrospectResponse {
    private boolean isValid;
    private String userId;
    private String username;
    private List<String> roleName;
    private Date expiresAt;
}
