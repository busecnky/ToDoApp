package com.authmicroservice.service;

import com.authmicroservice.dto.request.UserRequestDto;
import com.authmicroservice.entity.User;
import com.authmicroservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;
    private UserDetailsServiceImpl userDetailsService;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        authenticationManager = mock(AuthenticationManager.class);
        jwtService = mock(JwtService.class);
        userDetailsService = mock(UserDetailsServiceImpl.class);

        authService = new AuthService(
                userRepository,
                passwordEncoder,
                authenticationManager,
                jwtService,
                userDetailsService
        );
    }

    @Test
    void register_ShouldEncodePassword_AndSaveUser() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setUsername("username");
        userRequestDto.setPassword("plainPass");
        userRequestDto.setEmail("username@example.com");

        when(passwordEncoder.encode("plainPass")).thenReturn("encodedPass");

        authService.register(userRequestDto);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User saved = userCaptor.getValue();
        assertEquals("username", saved.getUsername());
        assertEquals("encodedPass", saved.getPassword());
        assertEquals("username@example.com", saved.getEmail());
    }

    @Test
    void login_ShouldAuthenticate_AndReturnToken() {
        UserRequestDto dto = new UserRequestDto();
        dto.setUsername("username");
        dto.setPassword("mypassword");

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("username");

        when(userDetailsService.loadUserByUsername("username")).thenReturn(userDetails);
        when(jwtService.generateToken("username")).thenReturn("mocked.jwt.token");

        String token = authService.login(dto);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken("username");

        assertEquals("mocked.jwt.token", token);
    }

    @Test
    void login_ShouldThrow_WhenAuthenticationFails() {
        UserRequestDto dto = new UserRequestDto();
        dto.setUsername("username");
        dto.setPassword("wrong");

        doThrow(new RuntimeException("Bad credentials"))
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> authService.login(dto));
        assertTrue(ex.getMessage().contains("Bad credentials"));
    }
}