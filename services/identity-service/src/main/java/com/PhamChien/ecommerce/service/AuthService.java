package com.PhamChien.ecommerce.service;

import com.PhamChien.ecommerce.dto.request.*;
import com.PhamChien.ecommerce.dto.response.AccountResponse;
import com.PhamChien.ecommerce.dto.response.IntrospectResponse;
import com.PhamChien.ecommerce.dto.response.TokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface AuthService {
    TokenResponse authenticate(LoginRequest request, HttpServletResponse response);

    AccountResponse getAccount();

    List<AccountResponse> findAll();

    AccountResponse registerUser(RegisterRequest registerRequest);

    AccountResponse activateAccount(String verificationCode);

    IntrospectResponse introspect(IntrospectRequest request);

    TokenResponse refresh(HttpServletRequest request);

    String logout(HttpServletResponse response);

    String forgotPassword(String email);

    String resetPassword(String resetKey);

    String changePassword(ResetPasswordRequest resetPasswordRequest);

    String assignRole(AssignRoleRequest request);

    String revokeRole(AssignRoleRequest request);

    IntrospectResponse introspectToken(HttpServletRequest request);

    AccountResponse findById(String accountId);

    String deleteAccount(String accountId);
}
