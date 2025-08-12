package com.java.user.service;

import java.util.Optional;

import com.java.user.model.User;

public interface UserService {

    public Optional<User> findByUsername(String username);

    public User register(User user);

}