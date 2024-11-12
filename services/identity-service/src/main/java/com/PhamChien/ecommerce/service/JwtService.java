package com.PhamChien.ecommerce.service;

import com.PhamChien.ecommerce.domain.Account;
import com.PhamChien.ecommerce.util.TokenType;

import java.util.Date;

public interface JwtService {
    String generateAccessToken(Account account);

    String generateRefreshToken(Account account);

    String generateResetToken(Account account);

    String extractUsername(String Token, TokenType type);

    Date extractExpiresAt(String token, TokenType type);

    boolean isValidToken(String token, TokenType type, Account account);
}
