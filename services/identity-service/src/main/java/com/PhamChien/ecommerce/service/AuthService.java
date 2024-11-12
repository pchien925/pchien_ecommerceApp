package com.PhamChien.ecommerce.service;

import com.PhamChien.ecommerce.dto.request.*;
import com.PhamChien.ecommerce.dto.response.AccountResponse;
import com.PhamChien.ecommerce.dto.response.IntrospectResponse;
import com.PhamChien.ecommerce.dto.response.TokenResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface AuthService {
    TokenResponse authenticate(LoginRequest request);

    AccountResponse getAccount();

    List<AccountResponse> findAll();

    AccountResponse registerUser(RegisterRequest registerRequest);

    AccountResponse activateAccount(String verificationCode);

    IntrospectResponse introspect(IntrospectRequest request);

    TokenResponse refresh(HttpServletRequest request);

    String logout(String request);

    String forgotPassword(String email);

    String resetPassword(String resetKey);

    String changePassword(ResetPasswordRequest resetPasswordRequest);

    String assignRole(AssignRoleRequest request);

    String revokeRole(AssignRoleRequest request);
}
