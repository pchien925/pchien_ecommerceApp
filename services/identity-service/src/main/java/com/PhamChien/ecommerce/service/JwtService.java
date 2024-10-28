package com.PhamChien.ecommerce.service;

import com.PhamChien.ecommerce.domain.UserCredential;
import com.nimbusds.jwt.JWTClaimsSet;

public interface JwtService {
    String generateAccessToken(UserCredential userCredential);

    String generateRefreshToken(UserCredential userCredential);

    String extractUsername(String Token);

    boolean isValidToken(String token, UserCredential userCredential);

    JWTClaimsSet jwtClaimsSet(String token);
}
