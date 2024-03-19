package com.java.nuven.domain.service;

import com.java.nuven.application.dto.LoginDTO;
import com.java.nuven.application.dto.LoginResponseDTO;
import com.java.nuven.domain.entity.User;
import com.java.nuven.domain.exception.DomainException;
import com.java.nuven.domain.exception.ErrorCode;
import com.java.nuven.infra.utils.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthenticationService {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtils jwtUtils;

    public AuthenticationService(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = new JwtUtils();
    }

    public LoginResponseDTO authenticate(LoginDTO loginDTO) {
        User user = userService.findByUsername(loginDTO.getUsername());
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new DomainException(ErrorCode.INVALID_CREDENTIALS, HttpStatus.BAD_REQUEST);
        }
        return new LoginResponseDTO(jwtUtils.generateToken(user.getUsername()));
    }
}
