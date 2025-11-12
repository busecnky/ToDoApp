package com.authmicroservice.controller;

import com.authmicroservice.dto.request.UserRequestDto;
import com.authmicroservice.entity.User;
import com.authmicroservice.service.AuthService;
import com.authmicroservice.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;


    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRequestDto userRequestDto) {
        User user = authService.register(userRequestDto);
        String token = jwtService.generateToken(user.getUsername());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserRequestDto userRequestDto) {
        String token = authService.login(userRequestDto);
        return ResponseEntity.ok(token);
    }
}
