package com.apigatewaymicroservice.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.function.Function;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.noContent;

@Configuration
public class GatewayConfig {

    @Bean
    public RouterFunction<ServerResponse> gatewayRouterFunctionsAddReqHeader() {
        return route()
                .GET("/api/todo/**", addAuthHeaderFilter())
                .POST("/api/todo/**", addAuthHeaderFilter())
                .PUT("/api/todo/**", addAuthHeaderFilter())
                .DELETE("/api/todo/**", addAuthHeaderFilter())
                .before(addRequestHeader())
                .build();
    }

    private Function<ServerRequest, ServerRequest> addRequestHeader() {
        return request -> {
            String token = request.headers().firstHeader("Authorization");

            if (token != null) {
                return ServerRequest.from(request)
                        .header("Authorization", token)
                        .build();
            }

            return request;
        };
    }

    private HandlerFunction<ServerResponse> addAuthHeaderFilter() {
        return request -> {
            System.out.println("Authorization Header: " + request.headers().firstHeader("Authorization"));
            return noContent().build();
        };
    }
}