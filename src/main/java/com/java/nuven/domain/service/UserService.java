package com.java.nuven.domain.service;

import com.java.nuven.application.dto.Params;
import com.java.nuven.domain.entity.User;
import com.java.nuven.domain.exception.DomainException;
import com.java.nuven.domain.exception.ErrorCode;
import com.java.nuven.domain.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public Page<User> findAll(Params params) {
        return userRepository.findAll(params);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new DomainException(ErrorCode.USER_NOT_FOUND, HttpStatus.BAD_REQUEST)
        );
    }

}
