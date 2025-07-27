package com.authmicroservice.controller;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/oauth2")
public class JwkSetController {

    private final RSAKey rsaKey;

    public JwkSetController(RSAKey rsaKey) {
        this.rsaKey = rsaKey;
    }

    @GetMapping("/jwks")
    public Map<String, Object> getKeys() {
        return new JWKSet(rsaKey.toPublicJWK()).toJSONObject();
    }
}
