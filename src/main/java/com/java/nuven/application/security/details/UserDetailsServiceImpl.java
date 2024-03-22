package com.java.nuven.application.security.details;

import com.java.nuven.domain.entity.User;
import com.java.nuven.domain.exception.DomainException;
import com.java.nuven.domain.exception.ErrorCode;
import com.java.nuven.domain.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new DomainException(ErrorCode.USER_NOT_FOUND, HttpStatus.BAD_REQUEST));
        return new UserDetailsImpl(user);
    }
}
