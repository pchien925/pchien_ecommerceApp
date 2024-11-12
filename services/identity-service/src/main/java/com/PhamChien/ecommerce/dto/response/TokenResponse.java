package com.PhamChien.ecommerce.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private String accountId;
}
