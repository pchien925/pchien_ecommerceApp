package com.PhamChien.ecommerce.service.Impl;

import com.PhamChien.ecommerce.domain.UserCredential;
import com.PhamChien.ecommerce.service.JwtService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.expiryHour}")
    private Long expiryHour;

    @Value("${jwt.expiryDay}")
    private Long expiryDay;

    @Value("${jwt.secretKey}")
    private String secretKey;


    @Override
    public String generateAccessToken(UserCredential userCredential) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(userCredential.getUsername())
                .issuer("PhamChien")
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * expiryHour)))
                .jwtID(UUID.randomUUID().toString())
                .claim("userID", userCredential.getId())
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
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(userCredential.getUsername())
                .issuer("PhamChien")
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * expiryDay)))
                .jwtID(UUID.randomUUID().toString())
                .claim("userID", userCredential.getId())
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
    public String extractUsername(String token){
        return jwtClaimsSet(token).getSubject();
    }



    @Override
    public boolean isValidToken(String token, UserCredential userCredential){
        String username = jwtClaimsSet(token).getSubject();
        return (username.equals(userCredential.getUsername()) && !isTokenExpired(token));
    }

    @Override
    public JWTClaimsSet jwtClaimsSet(String token){
        try {
            JWSVerifier jwsVerifier = new MACVerifier(secretKey.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet();
        }
        catch (JOSEException | ParseException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    private boolean isTokenExpired(String token){
        return jwtClaimsSet(token).getExpirationTime().before(new Date());
    }
}
