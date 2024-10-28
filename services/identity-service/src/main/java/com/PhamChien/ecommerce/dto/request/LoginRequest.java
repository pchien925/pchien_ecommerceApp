package com.PhamChien.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequest {
    @NotBlank(message = "username must be not null")
    private String username;

    @NotBlank(message = "password must be not blank")
    private String password;
}
