package com.PhamChien.api_gateway.configuration;

import com.PhamChien.api_gateway.dto.response.ApiResponse;
import com.PhamChien.api_gateway.service.IdentityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class AuthenticationFilter implements GlobalFilter, Ordered {
    IdentityService identityService;
    private final ObjectMapper jacksonObjectMapper;

    @NonFinal
    private String[] PUBLIC_ENDPOINTS = {"/api/v1/auth/login", "/api/v1/auth/register", "/api/v1/auth/activate-account", "/api/v1/auth/verifyToken", "/api/v1/auth/refresh", "/api/v1/auth/forgot-password", "/api/v1/auth/reset-password", "/api/v1/auth/change-password"};


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("---Authentication global filter----");

        ServerHttpRequest request = exchange.getRequest();
        String url = request.getPath().toString();
        //xử lí publicEndpoint
        if(isWhiteListURL(url) || url.contains("/v3/api-docs/")){
            return chain.filter(exchange);
        }

        //get token
        List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if(CollectionUtils.isEmpty(authHeader)){
            return unauthenticated(exchange.getResponse());
        }

        String token = authHeader.getFirst().replace("Bearer ", "");
        log.info("Token: " + token);

        return identityService.introspect(token).flatMap(introspectResponseApiResponse ->
        {
            if (introspectResponseApiResponse.getData().isValid()) {
                log.info("Authentication Successful");
                return chain.filter(exchange);
            }
            else {
                log.info("Authentication Failed");
                return unauthenticated(exchange.getResponse());
            }
        }).onErrorResume(throwable -> unauthenticated(exchange.getResponse()));
    }

    @Override
    public int getOrder() {
        return -1;
    }

    private boolean isWhiteListURL(String url) {
        List<String> permitUrls = List.of(PUBLIC_ENDPOINTS);

        return permitUrls.contains(url);
    }

    Mono<Void> unauthenticated(ServerHttpResponse response) {
        log.info("Unauthenticated");
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .data("Unauthenticated")
                .build();
        String body = "";
        try {
            body = jacksonObjectMapper.writeValueAsString(apiResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }
}
