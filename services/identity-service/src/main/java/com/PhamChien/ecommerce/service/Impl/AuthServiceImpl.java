package com.PhamChien.ecommerce.service.Impl;

import com.PhamChien.ecommerce.domain.Account;
import com.PhamChien.ecommerce.domain.Token;
import com.PhamChien.ecommerce.dto.request.*;
import com.PhamChien.ecommerce.dto.response.AccountResponse;
import com.PhamChien.ecommerce.dto.response.IntrospectResponse;
import com.PhamChien.ecommerce.dto.response.TokenResponse;
import com.PhamChien.ecommerce.exception.InvalidDataException;
import com.PhamChien.ecommerce.exception.ResourceNotFoundException;
import com.PhamChien.ecommerce.exception.UnauthenticatedException;
import com.PhamChien.ecommerce.mapper.AccountMapper;
import com.PhamChien.ecommerce.repository.AccountRepository;
import com.PhamChien.ecommerce.repository.RoleRepository;
import com.PhamChien.ecommerce.service.*;
import com.PhamChien.ecommerce.util.RoleName;
import com.PhamChien.ecommerce.util.TokenType;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final RoleService roleService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.topic.mailRegister}")
    private String mailRegister;

    @Value("${spring.kafka.topic.forgotPassword}")
    private String forgotPassword;

    @Override
    public TokenResponse authenticate(LoginRequest request, HttpServletResponse response){
        var account = accountRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Username not correct"));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), account.getPassword());

        if (!authenticated) throw new UnauthenticatedException("Unauthenticated");

        String accessToken = jwtService.generateAccessToken(account);
        String refreshToken = jwtService.generateRefreshToken(account);

        tokenService.save(Token.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .username(account.getUsername())
                .build());

        // Add refresh token to cookie
        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(60 * 60 * 24 * 7); // 7 days

        response.addCookie(refreshTokenCookie);
        return TokenResponse.builder()
                .accessToken(accessToken)
                .accountId(account.getId())
                .build();
    }
    @Override
    public AccountResponse getAccount(){

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Jwt object =(Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<Account> account = accountRepository.findByUsername(username);
        if (account.isEmpty()) {
            throw new ResourceNotFoundException("Account not found with username: " + username);
        }
        AccountResponse response = accountMapper.toAccountResponse(account.get());

        response.setRole(List.of(object.getClaimAsString("roles")));

        return response;
    }

    @Override
    public List<AccountResponse> findAll(){
        List<AccountResponse> list = accountRepository.findAll().stream().map(accountMapper::toAccountResponse).toList();

        for (var l : list){
            List<String> roleNameList = roleService.getRoleNameList(l.getId()).stream().map(RoleName::name).collect(Collectors.toList());
            l.setRole(roleNameList);
        }
        return list;
    }

    @Override
    public AccountResponse registerUser(RegisterRequest registerRequest) {
        if(accountRepository.existsByEmail(registerRequest.getEmail()))
            throw new ResourceNotFoundException("Email already exists");
        if(accountRepository.existsByUsername(registerRequest.getUsername()))
            throw new ResourceNotFoundException("Username already exists");

        Account account = Account.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .email(registerRequest.getEmail())
                .isActive(false)
                .verificationCode(UUID.randomUUID().toString())
                .verificationCodeExpiry(LocalDateTime.now().plusMinutes(30))
                .build();
        kafkaTemplate.send(mailRegister, String.format("email=%s,username=%s,code=%s", account.getEmail(), account.getUsername(), account.getVerificationCode()));
        AccountResponse response = accountMapper.toAccountResponse(accountRepository.save(account));
        roleService.assignRole(AssignRoleRequest.builder()
                .accountId(response.getId())
                .roleId(roleService.getRoleByRoleName(RoleName.ROLE_USER).getId())
                .build());
        return response;
    }

    @Override
    public AccountResponse activateAccount(String verificationCode) {
        Account account = accountRepository.findByVerificationCode(verificationCode).orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        if(account.getVerificationCodeExpiry().isBefore(LocalDateTime.now())) {
            account.setVerificationCode(UUID.randomUUID().toString());
            account.setVerificationCodeExpiry(LocalDateTime.now().plusMinutes(30));
            kafkaTemplate.send(mailRegister, String.format("email=%s,id=%s,code=%s", account.getEmail(), account.getId(), account.getVerificationCode()));

            throw new RuntimeException("Activation Token has expired, a new activation token has been sent");
        }

        Account user = accountRepository.findByVerificationCode(verificationCode)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found"));

        user.setIsActive(true);
        user.setVerificationCode(null);
        user.setVerificationCodeExpiry(null);
        return accountMapper.toAccountResponse(accountRepository.save(account));
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest request){
        String token = request.getToken();
        if(StringUtils.isBlank(token))
            throw new InvalidDataException("token must be not blank");

        final String username = jwtService.extractUsername(token, TokenType.ACCESS_TOKEN);
        Optional<Account> account = accountRepository.findByUsername(username);
        if (account.isEmpty())
            return IntrospectResponse.builder()
                    .isValid(false)
                    .build();
        List<String> roleNameList = roleService.getRoleNameList(account.get().getId()).stream().map(RoleName::name).collect(Collectors.toList());

        if(jwtService.isValidToken(token, TokenType.ACCESS_TOKEN, account.get())) {
            return IntrospectResponse.builder()
                    .accountId(account.get().getId())
                    .username(account.get().getUsername())
                    .roleName(roleNameList)
                    .expiresAt(jwtService.extractExpiresAt(token, TokenType.ACCESS_TOKEN))
                    .isValid(true)
                    .build();
        }
        throw new UnauthenticatedException("Invalid token");
    }

    @Override
    public TokenResponse refresh(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        String token = "";
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // Tìm cookie với tên là "refresh_token"
                if ("refresh_token".equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
        }
        log.info("Token: " + token);

        if(StringUtils.isBlank(token))
            throw new InvalidDataException("token must be not blank");

        final String username = jwtService.extractUsername(token, TokenType.REFRESH_TOKEN);

        Account account = accountRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        if(!jwtService.isValidToken(token, TokenType.REFRESH_TOKEN, account)){
            throw new InvalidDataException("Invalid token");
        }

        String accessToken = jwtService.generateAccessToken(account);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .accountId(account.getId())
                .build();
    }

    @Override
    public String logout(HttpServletResponse response){
        Cookie refreshTokenCookie = new Cookie("refresh_token", null);
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);

        response.addCookie(refreshTokenCookie);

        return "Logout";
    }

    @Override
    public String forgotPassword(String email){
        Optional<Account> account = accountRepository.findByEmail(email);
        if(account.isEmpty()){
            throw new InvalidDataException("Email does not exist");
        }

        // generate reset token
        String resetToken = jwtService.generateResetToken(account.get());

        // save to db
        tokenService.save(Token.builder()
                        .username(account.get().getUsername())
                        .resetToken(resetToken)
                .build());

        // TODO send email to user
        String confirmLink = String.format("curl --location 'http://localhost:8083/api/v1/auth/reset-password' \\\n" +
                "--header 'accept: */*' \\\n" +
                "--header 'Content-Type: application/json' \\\n" +
                "--data '%s'", resetToken);
        log.info("--> confirmLink: {}", confirmLink);

        kafkaTemplate.send(forgotPassword, String.format("email=%s,body=%s",account.get().getEmail(), confirmLink));


        return resetToken;
    }

    @Override
    public String resetPassword(String resetKey){
        log.info("---------- resetPassword ----------");

        // validate token
        var account = validateToken(resetKey);

        // check token by username
        tokenService.getByUsername(account.getUsername());

        return "Reset";
    }

    @Override
    public String changePassword(ResetPasswordRequest resetPasswordRequest) {
        if (!resetPasswordRequest.getPassword().equals(resetPasswordRequest.getConfirmPassword())){
            throw new InvalidDataException("Passwords do not match");
        }
        var account = validateToken(resetPasswordRequest.getKey());

        account.setPassword(passwordEncoder.encode(resetPasswordRequest.getPassword()));
        accountRepository.save(account);
        tokenService.delete(tokenService.getByUsername(account.getUsername()));
        return "Password changed";
    }

    private Account validateToken(String token) {
        // validate token
        var userName = jwtService.extractUsername(token, TokenType.RESET_TOKEN);

        // validate user is active or not
        var user = accountRepository.findByUsername(userName).orElseThrow(() -> new ResourceNotFoundException("Username not found"));
        if (!user.getIsActive()) {
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

    @Override
    public IntrospectResponse introspectToken(HttpServletRequest request) {

        String token = request.getHeader("Authorization").replace("Bearer ", "");
        if(StringUtils.isBlank(token))
            throw new UnauthenticatedException("token must be not blank");

        final String username = jwtService.extractUsername(token, TokenType.ACCESS_TOKEN);
        Optional<Account> account = accountRepository.findByUsername(username);
        if (account.isEmpty())
            return IntrospectResponse.builder()
                    .isValid(false)
                    .build();
        List<String> roleNameList = roleService.getRoleNameList(account.get().getId()).stream().map(RoleName::name).collect(Collectors.toList());

        if(jwtService.isValidToken(token, TokenType.ACCESS_TOKEN, account.get())) {
            return IntrospectResponse.builder()
                    .accountId(account.get().getId())
                    .username(account.get().getUsername())
                    .roleName(roleNameList)
                    .expiresAt(jwtService.extractExpiresAt(token, TokenType.ACCESS_TOKEN))
                    .isValid(true)
                    .build();
        }
        throw new UnauthenticatedException("Invalid token");
    }

    @Override
    public AccountResponse findById(String accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        List<RoleName> roleNameList = roleService.getRoleNameList(accountId);
        AccountResponse response = accountMapper.toAccountResponse(account);

        response.setRole(roleNameList.stream().map(RoleName::name).toList());

        return response;
    }

    @Override
    public String deleteAccount(String accountId) {
        accountRepository.deleteById(accountId);
        return "deleted";
    }
}
