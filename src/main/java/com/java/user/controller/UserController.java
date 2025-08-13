package com.java.user.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java.user.dto.ApiResponse;
import com.java.user.dto.LoginRequest;
import com.java.user.dto.UserDTO;
import com.java.user.model.User;
import com.java.user.service.UserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/users")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginRequest request) {
        ApiResponse<String> response = userService.login(request);
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }
    
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserDTO>> getUserDetailInSession(Authentication auth) {
        ApiResponse<UserDTO> response = userService.getUserDetail(auth.getName());
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }

    @GetMapping("/profile/{username}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserDetail(@PathVariable String username) {
        ApiResponse<UserDTO> response = userService.getUserDetail(username);
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        ApiResponse<List<UserDTO>> response = userService.getAll();
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }
    

}
