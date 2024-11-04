package com.PhamChien.ecommerce.service.Impl;

import com.PhamChien.ecommerce.domain.Token;
import com.PhamChien.ecommerce.domain.UserCredential;
import com.PhamChien.ecommerce.dto.request.*;
import com.PhamChien.ecommerce.dto.response.IntrospectResponse;
import com.PhamChien.ecommerce.dto.response.TokenResponse;
import com.PhamChien.ecommerce.dto.response.UserCredentialResponse;
import com.PhamChien.ecommerce.exception.InvalidDataException;
import com.PhamChien.ecommerce.exception.ResourceNotFoundException;
import com.PhamChien.ecommerce.mapper.UserCredentialMapper;
import com.PhamChien.ecommerce.repository.RoleRepository;
import com.PhamChien.ecommerce.repository.UserCredentialHasRoleRepository;
import com.PhamChien.ecommerce.repository.UserCredentialRepository;
import com.PhamChien.ecommerce.service.*;
import com.PhamChien.ecommerce.util.RoleName;
import com.PhamChien.ecommerce.util.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserCredentialRepository userCredentialRepository;
    private final EmailService emailService;
    private final UserCredentialMapper userCredentialMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserCredentialHasRoleRepository userCredentialHasRoleRepository;
    private final RoleRepository roleRepository;
    private final RoleService roleService;

    @Override
    public TokenResponse authenticate(LoginRequest request){
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        if(!authenticate.isAuthenticated()){
            throw new ResourceNotFoundException("Invalid username or password");
        }
        UserCredential userCredential = (UserCredential) authenticate.getPrincipal();
        String accessToken = jwtService.generateAccessToken(userCredential);
        String refreshToken = jwtService.generateRefreshToken(userCredential);

        tokenService.save(Token.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .username(userCredential.getUsername())
                .build());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(userCredential.getId())
                .build();
    }
    @Override
    public UserCredentialResponse getUserCredential(){

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<UserCredential> userCredential = userCredentialRepository.findByUsername(username);
        if (userCredential.isEmpty()) {
            throw new ResourceNotFoundException("User Credential not found with username: " + username);
        }
        List<String> roleNameList = roleService.getRoleNameList(userCredential.get().getId()).stream().map(RoleName::name).collect(Collectors.toList());
        UserCredentialResponse response = userCredentialMapper.toUserCredentialResponse(userCredential.get());

        response.setRole(roleNameList);

        return response;
    }

    @Override
    public List<UserCredentialResponse> findAll(){
        List<UserCredentialResponse> list = userCredentialRepository.findAll().stream().map(userCredentialMapper::toUserCredentialResponse).toList();

        for (var l : list){
            List<String> roleNameList = roleService.getRoleNameList(l.getId()).stream().map(RoleName::name).collect(Collectors.toList());
            l.setRole(roleNameList);
        }
        return list;
    }

    @Override
    public UserCredentialResponse registerUser(RegisterRequest registerRequest) {
        if(userCredentialRepository.existsByEmail(registerRequest.getEmail()))
            throw new ResourceNotFoundException("Email already exists");
        if(userCredentialRepository.existsByUsername(registerRequest.getUsername()))
            throw new ResourceNotFoundException("Username already exists");

        UserCredential userCredential  = UserCredential.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .email(registerRequest.getEmail())
                .isActive(false)
                .verificationCode(UUID.randomUUID().toString())
                .verificationCodeExpiry(LocalDateTime.now().plusMinutes(30))
                .build();

        emailService.sendSimpleMail(SendMailRequest.builder()
                .recipient(userCredential.getEmail())
                .subject("acctive account")
                .msgBody("http://localhost:8081/api/v1/auth/activate-account?code=" + userCredential.getVerificationCode())
                .build());

        return userCredentialMapper.toUserCredentialResponse(userCredentialRepository.save(userCredential));
    }

    @Override
    public UserCredentialResponse activateAccount(String verificationCode) {
        UserCredential userCredential = userCredentialRepository.findByVerificationCode(verificationCode).orElseThrow(() -> new ResourceNotFoundException("User Credential not found"));
        if(userCredential.getVerificationCodeExpiry().isBefore(LocalDateTime.now())) {
            emailService.sendSimpleMail(SendMailRequest.builder()
                    .recipient(userCredential.getEmail())
                    .subject("acctive account")
                    .msgBody("http://localhost:8081/api/v1/auth/activate-account?verificationCode=" + userCredential.getVerificationCode())
                    .build());
            throw new RuntimeException("Activation Token has expired, a new activation token has been sent");
        }

        UserCredential user = userCredentialRepository.findByVerificationCode(verificationCode)
                .orElseThrow(() -> new UsernameNotFoundException("User Credential not found"));

        user.setIsActive(true);
        user.setVerificationCode(null);
        user.setVerificationCodeExpiry(null);
        return userCredentialMapper.toUserCredentialResponse(userCredentialRepository.save(userCredential));
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest request){
        String token = request.getToken();
        if(StringUtils.isBlank(token))
            throw new InvalidDataException("token must be not blank");

        final String username = jwtService.extractUsername(token, TokenType.ACCESS_TOKEN);
        Optional<UserCredential> userCredential = userCredentialRepository.findByUsername(username);
        if (userCredential.isEmpty())
            return IntrospectResponse.builder()
                    .isValid(false)
                    .build();
        List<String> roleNameList = roleService.getRoleNameList(userCredential.get().getId()).stream().map(RoleName::name).collect(Collectors.toList());

        if(jwtService.isValidToken(token, TokenType.ACCESS_TOKEN, userCredential.get())) {
            return IntrospectResponse.builder()
                    .userId(userCredential.get().getId())
                    .username(userCredential.get().getUsername())
                    .roleName(roleNameList)
                    .expiresAt(jwtService.extractExpiresAt(token, TokenType.ACCESS_TOKEN))
                    .isValid(true)
                    .build();
        }
        throw new ResourceNotFoundException("Invalid token");
    }

    @Override
    public TokenResponse refresh(HttpServletRequest request){
        String token = request.getHeader("refresh-token");

        if(StringUtils.isBlank(token))
            throw new InvalidDataException("token must be not blank");

        final String username = jwtService.extractUsername(token, TokenType.REFRESH_TOKEN);

        UserCredential userCredential = userCredentialRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User credential not found"));

        if(!jwtService.isValidToken(token, TokenType.REFRESH_TOKEN, userCredential)){
            throw new InvalidDataException("Invalid token");
        }

        String accessToken = jwtService.generateAccessToken(userCredential);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(token)
                .userId(userCredential.getId())
                .build();
    }

    @Override
    public String logout(HttpServletRequest request){
        String token = request.getHeader("REFERER");

        if(StringUtils.isBlank(token))
            throw new InvalidDataException("token must be not blank");

        final String username = jwtService.extractUsername(token, TokenType.REFRESH_TOKEN);

        Token curToken = tokenService.getByUsername(username);

        return tokenService.delete(curToken);
    }

    @Override
    public String forgotPassword(String email){
        Optional<UserCredential> userCredential = userCredentialRepository.findByEmail(email);
        if(userCredential.isEmpty()){
            throw new InvalidDataException("Email does not exist");
        }

        // generate reset token
        String resetToken = jwtService.generateResetToken(userCredential.get());

        // save to db
        tokenService.save(Token.builder()
                        .username(userCredential.get().getUsername())
                        .resetToken(resetToken)
                .build());

        // TODO send email to user
        String confirmLink = String.format("curl --location 'http://localhost:8083/api/v1/auth/reset-password' \\\n" +
                "--header 'accept: */*' \\\n" +
                "--header 'Content-Type: application/json' \\\n" +
                "--data '%s'", resetToken);
        log.info("--> confirmLink: {}", confirmLink);

        emailService.sendSimpleMail(SendMailRequest.builder()
                .recipient(userCredential.get().getEmail())
                .subject("reset password")
                .msgBody(confirmLink)
                .build());

        return resetToken;
    }

    @Override
    public String resetPassword(String resetKey){
        log.info("---------- resetPassword ----------");

        // validate token
        var userCredential = validateToken(resetKey);

        // check token by username
        tokenService.getByUsername(userCredential.getUsername());

        return "Reset";
    }

    @Override
    public String changePassword(ResetPasswordRequest resetPasswordRequest) {
        if (!resetPasswordRequest.getPassword().equals(resetPasswordRequest.getConfirmPassword())){
            throw new InvalidDataException("Passwords do not match");
        }
        var userCredential = validateToken(resetPasswordRequest.getKey());

        userCredential.setPassword(passwordEncoder.encode(resetPasswordRequest.getPassword()));
        userCredentialRepository.save(userCredential);
        tokenService.delete(tokenService.getByUsername(userCredential.getUsername()));
        return "Password changed";
    }

    private UserCredential validateToken(String token) {
        // validate token
        var userName = jwtService.extractUsername(token, TokenType.RESET_TOKEN);

        // validate user is active or not
        var user = userCredentialRepository.findByUsername(userName).orElseThrow(() -> new ResourceNotFoundException("Username not found"));
        if (!user.isEnabled()) {
            throw new InvalidDataException("User not active");
        }

        return user;
    }

    @Override
    public String assignRole(AssignRoleRequest request) {
        return roleService.assignRole(request);
    }

    @Override
    public String revokeRole(AssignRoleRequest request){
        return roleService.revokeRole(request);
    }

}
