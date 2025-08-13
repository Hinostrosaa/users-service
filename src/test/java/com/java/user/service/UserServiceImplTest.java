package com.java.user.service;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.java.user.config.JWTUtil;
import com.java.user.dto.ApiResponse;
import com.java.user.dto.LoginRequest;
import com.java.user.model.User;
import com.java.user.repository.UserRepository;

public class UserServiceImplTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTUtil jwtUtil;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("Test");
        user.setUsername("test@idat.pe");
        user.setPassword("1234");
        user.setRole("admin");
        when(passwordEncoder.encode("1234")).thenReturn("Password1234");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User savedUser = userServiceImpl.register(user);

        //Assert
        assertNotNull(savedUser);
        assertEquals("Password1234", savedUser.getPassword());
        verify(passwordEncoder, times(1)).encode("1234");
        verify(userRepository, times(1)).save(user);

    }

    @Test
    void testLoginSuccess() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("richard");
        loginRequest.setPassword("1234");

        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("richard");
        when(userDetails.getAuthorities()).thenReturn(List.of());
        when(jwtUtil.generateToken(userDetails.getUsername(), userDetails.getAuthorities())).thenReturn("token");

        ApiResponse<String> response = userServiceImpl.login(loginRequest);

        assertEquals(HttpStatus.OK.value(), response.getResponseCode());
        assertEquals("Login Exitoso", response.getResponseMessage());
        assertEquals("token", response.getData());
    }

    @Test
    void testLoginInvalidCredentials() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("invalid");
        loginRequest.setPassword("1234");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new org.springframework.security.core.AuthenticationException("ERROR") {
        });

        ApiResponse<String> response = userServiceImpl.login(loginRequest);

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getResponseCode());
        assertEquals("Credenciales Invalidas", response.getResponseMessage());
        assertNull(response.getData());
    }

}

