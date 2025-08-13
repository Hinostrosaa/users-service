package com.java.user.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.java.user.config.JWTUtil;
import com.java.user.dto.ApiResponse;
import com.java.user.dto.LoginRequest;
import com.java.user.dto.UserDTO;
import com.java.user.dto.UserMapper;
import com.java.user.model.User;
import com.java.user.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtil;

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public ApiResponse<List<UserDTO>> getAll() {
        List<UserDTO> usersDTO = userRepository.findAll()
                                    .stream()
                                    .map(UserMapper::toDTO)
                                    .collect(Collectors.toList());    
        return ApiResponse.<List<UserDTO>>builder()
            .responseCode(HttpStatus.OK.value())
            .responseMessage("Usuarios obtenidos correctamente")
            .data(usersDTO).build();
    }

    @Override
    public ApiResponse<String> login(LoginRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            String token = jwtUtil.generateToken(userDetails.getUsername(), userDetails.getAuthorities());
            return new ApiResponse<>(HttpStatus.OK.value(), "Login Exitoso", token);
        } catch (AuthenticationException ex) {
            return new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), "Credenciales Invalidas", null);
        }
    }

    @Override
    public ApiResponse<UserDTO> getUserDetail(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            return ApiResponse.<UserDTO>builder()
                        .responseCode(HttpStatus.OK.value())
                        .responseMessage("Usuario encontrado")
                        .data(UserMapper.toDTO(user.get()))
                        .build();
        } else {
            return ApiResponse.<UserDTO>builder()
                        .responseCode(HttpStatus.NOT_FOUND.value())
                        .responseMessage("Usuario no encontrado")
                        .data(null)
                        .build();
        }
    }

    
}
