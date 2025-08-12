package com.java.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

}