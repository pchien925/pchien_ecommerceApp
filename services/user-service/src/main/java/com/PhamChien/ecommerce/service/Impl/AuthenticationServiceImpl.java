package com.PhamChien.ecommerce.service.Impl;

import com.PhamChien.ecommerce.domain.User;
import com.PhamChien.ecommerce.dto.request.AuthenticationRequest;
import com.PhamChien.ecommerce.dto.response.AuthenticationResponse;
import com.PhamChien.ecommerce.exception.AppException;
import com.PhamChien.ecommerce.exception.ErrorCode;
import com.PhamChien.ecommerce.repository.UserRepository;
import com.PhamChien.ecommerce.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        User user = userRepository.findByUsername(authenticationRequest.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return AuthenticationResponse.builder()
                .isAuthenticated(passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword()))
                .build();
    }
}
