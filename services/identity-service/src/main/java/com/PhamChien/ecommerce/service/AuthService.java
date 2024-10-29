package com.PhamChien.ecommerce.service;

import com.PhamChien.ecommerce.dto.request.LoginRequest;
import com.PhamChien.ecommerce.dto.request.RegisterRequest;
import com.PhamChien.ecommerce.dto.request.ResetPasswordRequest;
import com.PhamChien.ecommerce.dto.response.IntrospectResponse;
import com.PhamChien.ecommerce.dto.response.TokenResponse;
import com.PhamChien.ecommerce.dto.response.UserCredentialResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface AuthService {
    TokenResponse authenticate(LoginRequest request);

    UserCredentialResponse getUserCredential(HttpServletRequest request);

    List<UserCredentialResponse> findAll();

    UserCredentialResponse registerUser(RegisterRequest registerRequest);
    UserCredentialResponse activateAccount(String verificationCode);
    IntrospectResponse introspect(HttpServletRequest request);

    TokenResponse refresh(HttpServletRequest request);

    String logout(HttpServletRequest request);

    String forgotPassword(String email);

    String resetPassword(String resetKey);

    String changePassword(ResetPasswordRequest resetPasswordRequest);
}
