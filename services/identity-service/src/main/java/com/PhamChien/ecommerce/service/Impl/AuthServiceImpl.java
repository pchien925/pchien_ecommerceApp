package com.PhamChien.ecommerce.service.Impl;

import com.PhamChien.ecommerce.domain.Token;
import com.PhamChien.ecommerce.domain.UserCredential;
import com.PhamChien.ecommerce.dto.request.LoginRequest;
import com.PhamChien.ecommerce.dto.request.RegisterRequest;
import com.PhamChien.ecommerce.dto.request.SendMailRequest;
import com.PhamChien.ecommerce.dto.response.IntrospectResponse;
import com.PhamChien.ecommerce.dto.response.TokenResponse;
import com.PhamChien.ecommerce.dto.response.UserCredentialResponse;
import com.PhamChien.ecommerce.exception.InvalidDataException;
import com.PhamChien.ecommerce.exception.ResourceNotFoundException;
import com.PhamChien.ecommerce.mapper.UserCredentialMapper;
import com.PhamChien.ecommerce.repository.UserCredentialRepository;
import com.PhamChien.ecommerce.service.AuthService;
import com.PhamChien.ecommerce.service.EmailService;
import com.PhamChien.ecommerce.service.JwtService;
import com.PhamChien.ecommerce.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public UserCredentialResponse getUserCredential(String id){
        Optional<UserCredential> userCredential = userCredentialRepository.findById(id);
        if (userCredential.isEmpty()) {
            throw new ResourceNotFoundException("User Credential not found with ID: " + id);
        }
        return userCredentialMapper.toUserCredentialResponse(userCredential.get());
    }

    @Override
    public List<UserCredentialResponse> findAll(){
        return userCredentialRepository.findAll().stream().map(userCredentialMapper::toUserCredentialResponse).toList();
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
                .subject("verify account")
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
                    .subject("verify account")
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
    public IntrospectResponse introspect(HttpServletRequest request){
        String token = request.getHeader("Authorization");

        if(StringUtils.isBlank(token))
            throw new InvalidDataException("token must be not blank");

        final String username = jwtService.extractUsername(token);
        UserCredential userCredential = userCredentialRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Username not found"));
        if(jwtService.isValidToken(token, userCredential)) {
            return IntrospectResponse.builder()
                    .userId(userCredential.getId())
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

        final String username = jwtService.extractUsername(token);

        UserCredential userCredential = userCredentialRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User credential not found"));

        if(!jwtService.isValidToken(token, userCredential)){
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

        final String username = jwtService.extractUsername(token);

        Token curToken = tokenService.getByUsername(username);

        return tokenService.delete(curToken);
    }
}
