package com.PhamChien.ecommerce.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class RegisterRequest {

    @NotNull(message = "username must be not null")
    private String username;

    @NotNull(message = "password must be not null")
    @Size(min = 8, message = "password must be have 8 character")
    private String password;

    @Email(message = "email invalid format")
    private String email;

}
