package com.PhamChien.ecommerce.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
public class UserCredentialResponse {
    private String id;

    private String username;

    private String password;

    private String email;

    private Boolean isActive;

    private String verificationCode;

    private LocalDateTime verificationCodeExpiry;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
