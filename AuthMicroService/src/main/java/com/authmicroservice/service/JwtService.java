package com.authmicroservice.service;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    private final RSAKey rsaKey;

    public JwtService(RSAKey rsaKey) {
        this.rsaKey = rsaKey;
    }

    public String generateToken(String username) {
        try {
            JWSSigner signer = new RSASSASigner(rsaKey.toPrivateKey());

            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .subject(username)
                    .issuer("auth-service")
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + 3600_000))
                    .build();

            SignedJWT signedJWT = new SignedJWT(
                    new com.nimbusds.jose.JWSHeader.Builder(JWSAlgorithm.RS256)
                            .keyID(rsaKey.getKeyID())
                            .build(),
                    claims
            );

            signedJWT.sign(signer);
            return signedJWT.serialize();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate token", e);
        }
    }
}
