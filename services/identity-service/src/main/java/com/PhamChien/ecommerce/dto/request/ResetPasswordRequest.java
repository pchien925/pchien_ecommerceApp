package com.PhamChien.ecommerce.dto.request;

import lombok.Getter;

@Getter
public class ResetPasswordRequest {
    private String key;
    private String password;
    private String confirmPassword;
}
