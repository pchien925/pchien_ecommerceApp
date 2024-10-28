package com.PhamChien.ecommerce.service;

import com.PhamChien.ecommerce.domain.Token;
import com.PhamChien.ecommerce.exception.ResourceNotFoundException;
import com.PhamChien.ecommerce.repository.TokenRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public record TokenService(TokenRepository tokenRepository) {
    public Token getByUsername(String username) {
        return tokenRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Token not found"));
    }

    public long save(Token token) {
        Optional<Token> optional = tokenRepository.findByUsername(token.getUsername());
        if (optional.isEmpty()) {
            tokenRepository.save(token);
            return token.getId();
        } else {
            Token t = optional.get();
            t.setAccessToken(token.getAccessToken());
            t.setRefreshToken(token.getRefreshToken());
            tokenRepository.save(t);
            return t.getId();
        }
    }

    public String delete(Token token) {
        tokenRepository.delete(token);
        return "Deleted!";
    }

}
