package com.PhamChien.ecommerce.controller;

import com.PhamChien.ecommerce.domain.UserCredential;
import com.PhamChien.ecommerce.dto.request.*;
import com.PhamChien.ecommerce.dto.response.*;
import com.PhamChien.ecommerce.service.AuthService;
import com.PhamChien.ecommerce.service.EmailService;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final EmailService emailService;

    @GetMapping("/{userCredentialId}")
    public ApiResponse<UserCredentialResponse> getUserCredential(@PathVariable("userCredentialId") String userCredentialId){
        return ApiResponse.<UserCredentialResponse>builder()
                .status(HttpStatus.OK.value())
                .data(authService.getUserCredential(userCredentialId))
                .build();
    }

    @GetMapping
    public ApiResponse<List<UserCredentialResponse>> findAll(){
        return ApiResponse.<List<UserCredentialResponse>>builder()
                .status(HttpStatus.OK.value())
                .data(authService.findAll())
                .build();
    }


    @GetMapping("/activate-account")
    public ApiResponse<UserCredentialResponse> verifyUser(@RequestParam("code") String verificationCode){
        return ApiResponse.<UserCredentialResponse>builder()
                .data(authService.activateAccount(verificationCode))
                .build();
    }


    @PostMapping("/verifyToken")
    public ApiResponse<IntrospectResponse> verifyToken(HttpServletRequest request){
        return ApiResponse.<IntrospectResponse>builder()
                .data(authService.introspect(request))
                .build();
    }

    @PostMapping("/register")
    public ApiResponse<UserCredentialResponse> registerUser(@RequestBody RegisterRequest registerRequest) {
        UserCredentialResponse userCredentialResponse = authService.registerUser(registerRequest);
        return ApiResponse.<UserCredentialResponse>builder()
                .data(userCredentialResponse)
                .build();
    }

    @PostMapping("/login")
    public ApiResponse<TokenResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        return ApiResponse.<TokenResponse>builder()
                .status(HttpStatus.OK.value())
                .data(authService.authenticate(loginRequest))
                .build();
    }

    @PostMapping("/refresh")
    public ApiResponse<TokenResponse> refresh(HttpServletRequest request){
        return ApiResponse.<TokenResponse>builder()
                .status(HttpStatus.OK.value())
                .data(authService.refresh(request))
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(HttpServletRequest request){
     return ApiResponse.<String>builder()
             .status(HttpStatus.OK.value())
             .data(authService.logout(request))
             .build();
    }
}
