package com.PhamChien.api_gateway.service;

import com.PhamChien.api_gateway.dto.request.IntrospectRequest;
import com.PhamChien.api_gateway.dto.response.ApiResponse;
import com.PhamChien.api_gateway.dto.response.IntrospectResponse;
import com.PhamChien.api_gateway.repository.IdentityClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class IdentityService {
    private final IdentityClient identityClient;

    public Mono<ApiResponse<IntrospectResponse>> introspect(String token) {
        return identityClient.introspect(IntrospectRequest.builder()
                        .token(token)
                .build());
    }
}
