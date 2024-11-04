package com.PhamChien.ecommerce.service.Impl;

import com.PhamChien.ecommerce.domain.Role;
import com.PhamChien.ecommerce.domain.UserCredential;
import com.PhamChien.ecommerce.exception.InvalidDataException;
import com.PhamChien.ecommerce.repository.RoleRepository;
import com.PhamChien.ecommerce.service.JwtService;
import com.PhamChien.ecommerce.util.TokenType;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {

    private final RoleRepository roleRepository;
    @Value("${jwt.expiryHour}")
    private Long expiryHour;

    @Value("${jwt.expiryDay}")
    private Long expiryDay;

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.refreshKey}")
    private String refreshKey;

    @Value("${jwt.resetKey}")
    private String resetKey;

    public JwtServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    @Override
    public String generateAccessToken(UserCredential userCredential) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);

        List<Role> roles = roleRepository.findAllByUserCredentialId(userCredential.getId());

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(userCredential.getUsername())
                .issuer("PhamChien")
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * expiryHour)))
                .claim("userID", userCredential.getId())
                .claim("roles", buildScope(userCredential))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);


        try {
            jwsObject.sign(new MACSigner(secretKey.getBytes()));
            return jwsObject.serialize();
        }
        catch (JOSEException e) {
            log.error("can't generate token");
            throw new RuntimeException(e);
        }
    }

    @Override
    public String generateRefreshToken(UserCredential userCredential) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(userCredential.getUsername())
                .issuer("PhamChien")
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * expiryDay)))
                .claim("userID", userCredential.getId())
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);


        try {
            jwsObject.sign(new MACSigner(refreshKey.getBytes()));
            return jwsObject.serialize();
        }
        catch (JOSEException e) {
            log.error("can't generate token");
            throw new RuntimeException(e);
        }
    }

    @Override
    public String generateResetToken(UserCredential userCredential) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(userCredential.getUsername())
                .issuer("PhamChien")
                .expirationTime(new Date(System.currentTimeMillis() + (1000 * 60 * 60)))
                .claim("userID", userCredential.getId())
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);


        try {
            jwsObject.sign(new MACSigner(resetKey.getBytes()));
            return jwsObject.serialize();
        }
        catch (JOSEException e) {
            log.error("can't generate token");
            throw new RuntimeException(e);
        }
    }

    @Override
    public String extractUsername(String token, TokenType type){
        return jwtClaimsSet(token, type).getSubject();
    }

    @Override
    public Date extractExpiresAt(String token, TokenType type){
        return jwtClaimsSet(token, type).getExpirationTime();
    }


    @Override
    public boolean isValidToken(String token, TokenType type, UserCredential userCredential){
        String username = jwtClaimsSet(token, type).getSubject();
        return (username.equals(userCredential.getUsername()) && !isTokenExpired(token, type));
    }


    public JWTClaimsSet jwtClaimsSet(String token, TokenType tokenType){
        String key = key(tokenType);
        try {
            JWSVerifier jwsVerifier = new MACVerifier(key.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet();
        }
        catch (JOSEException | ParseException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    private boolean isTokenExpired(String token, TokenType type){
        return jwtClaimsSet(token, type).getExpirationTime().before(new Date());
    }

    private String key(TokenType type) {
        switch (type) {
            case ACCESS_TOKEN -> {
                return secretKey;
            }
            case REFRESH_TOKEN -> {
                return refreshKey;
            }
            case RESET_TOKEN -> {
                return resetKey;
            }
            default -> throw new InvalidDataException("Invalid token type");
        }
    }

    private String buildScope(UserCredential user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        List<Role> roles = roleRepository.findAllByUserCredentialId(user.getId());


        if (!CollectionUtils.isEmpty(user.getRoles()))
            roles.forEach(role -> {
                stringJoiner.add(role.getName().name());
            });

        return stringJoiner.toString();
    }
}
