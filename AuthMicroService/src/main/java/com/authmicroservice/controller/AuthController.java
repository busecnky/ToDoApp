package com.authmicroservice.controller;

import com.authmicroservice.dto.request.UserRequestDto;
import com.authmicroservice.entity.User;
import com.authmicroservice.service.AuthService;
import com.authmicroservice.service.JwtService;
import com.authmicroservice.service.UserDetailsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    public AuthController(AuthService authService, JwtService jwtService, AuthenticationManager authenticationManager,
                          UserDetailsServiceImpl userDetailsServiceImpl) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRequestDto userRequestDto) {
        User user = authService.register(userRequestDto);
        String token = jwtService.generateToken(user.getUsername());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserRequestDto userRequestDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequestDto.getUsername(),
                userRequestDto.getPassword()));
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(userRequestDto.getUsername());
        String token = jwtService.generateToken(userDetails.getUsername());
        return ResponseEntity.ok(token);
    }
}
