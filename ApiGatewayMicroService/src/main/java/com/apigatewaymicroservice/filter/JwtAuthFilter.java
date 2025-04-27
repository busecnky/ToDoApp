package com.apigatewaymicroservice.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter implements WebFilter {
    private static final String SECRET_KEY = "#luC}VB>IsC)*>&x*5*zMqIdD}Pct_%T3w>{9&Zl$tbX;ZwfF3J+p%iD~o]8-!^`";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (request.getURI().getPath().startsWith("/api/auth")) {
            return chain.filter(exchange);
        }

        String token = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (token == null || !token.startsWith("Bearer ")) {
            return exchange.getResponse().setComplete();
        }

        try {
            String jwtToken = token.substring(7);
            JWT.require(Algorithm.HMAC256(SECRET_KEY)).build().verify(jwtToken);
        } catch (JWTVerificationException e) {
            return exchange.getResponse().setComplete();
        }

        ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
            .header(HttpHeaders.AUTHORIZATION, token)
            .build();

        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }
}
