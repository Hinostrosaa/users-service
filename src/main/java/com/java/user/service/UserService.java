package com.java.user.service;

import java.util.List;
import java.util.Optional;

import com.java.user.dto.ApiResponse;
import com.java.user.dto.LoginRequest;
import com.java.user.dto.UserDTO;
import com.java.user.model.User;

public interface UserService {

    public ApiResponse<String> login(LoginRequest request);

    public ApiResponse<UserDTO> getUserDetail(String username);

    public Optional<User> findByUsername(String username);

    public User register(User user);

    public ApiResponse<List<UserDTO>> getAll();
    
}
