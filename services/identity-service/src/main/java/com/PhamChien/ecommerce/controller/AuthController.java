package com.PhamChien.ecommerce.controller;

import com.PhamChien.ecommerce.dto.request.*;
import com.PhamChien.ecommerce.dto.response.*;
import com.PhamChien.ecommerce.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;

    @GetMapping("/myInfo")
    public ApiResponse<AccountResponse> getUserCredential(){
        return ApiResponse.<AccountResponse>builder()
                .status(HttpStatus.OK.value())
                .data(authService.getAccount())
                .build();
    }

    @GetMapping
    public ApiResponse<List<AccountResponse>> findAll(){
        return ApiResponse.<List<AccountResponse>>builder()
                .status(HttpStatus.OK.value())
                .data(authService.findAll())
                .build();
    }


    @GetMapping("/activate-account")
    public ApiResponse<AccountResponse> verifyUser(@RequestParam("code") String verificationCode){
        return ApiResponse.<AccountResponse>builder()
                .data(authService.activateAccount(verificationCode))
                .build();
    }


    @PostMapping("/verifyToken")
    public ApiResponse<IntrospectResponse> verifyToken(@RequestBody IntrospectRequest request){
        return ApiResponse.<IntrospectResponse>builder()
                .data(authService.introspect(request))
                .build();
    }

    @PostMapping("/register")
    public ApiResponse<AccountResponse> registerUser(@RequestBody RegisterRequest registerRequest) {
        AccountResponse accountResponse = authService.registerUser(registerRequest);
        return ApiResponse.<AccountResponse>builder()
                .data(accountResponse)
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
    public ApiResponse<String> logout(@RequestHeader("Authorization") String authorizationHeader){
     return ApiResponse.<String>builder()
             .status(HttpStatus.OK.value())
             .data(authService.logout(authorizationHeader))
             .build();
    }

    @PostMapping("/forgot-password")
    public ApiResponse<String> forgotPassword(@RequestBody String email){
        return ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .data(authService.forgotPassword(email))
                .build();
    }

    @PostMapping("/reset-password")
    public ApiResponse<String> resetPassword(@RequestBody String resetKey){
        return ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .data(authService.resetPassword(resetKey))
                .build();
    }

    @PostMapping("/change-password")
    public ApiResponse<String> changePassword(@RequestBody ResetPasswordRequest resetPasswordRequest){
        return ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .data(authService.changePassword(resetPasswordRequest))
                .build();
    }

    @PostMapping("/assignRole")
    public ApiResponse<String> assignRole(@RequestBody AssignRoleRequest assignRoleRequest){
        return ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .data(authService.assignRole(assignRoleRequest))
                .build();
    }

    @PostMapping("/revokeRole")
    public ApiResponse<String> revokeRole(@RequestBody AssignRoleRequest assignRoleRequest){
        return ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .data(authService.revokeRole(assignRoleRequest))
                .build();
    }

}
