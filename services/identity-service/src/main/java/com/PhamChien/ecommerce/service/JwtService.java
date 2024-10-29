package com.PhamChien.ecommerce.service;

import com.PhamChien.ecommerce.domain.UserCredential;
import com.PhamChien.ecommerce.util.TokenType;
import com.nimbusds.jwt.JWTClaimsSet;

public interface JwtService {
    String generateAccessToken(UserCredential userCredential);

    String generateRefreshToken(UserCredential userCredential);

    String generateResetToken(UserCredential userCredential);

    String extractUsername(String Token, TokenType type);

    boolean isValidToken(String token, TokenType type, UserCredential userCredential);
}
