package com.java.nuven.infra.init;

import com.java.nuven.domain.repository.TodoRepository;
import com.java.nuven.domain.repository.UserRepository;
import com.java.nuven.domain.service.AuthenticationService;
import com.java.nuven.domain.service.TodoService;
import com.java.nuven.domain.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeanConfig {

    @Bean
    TodoService todoService(TodoRepository todoRepository) {
        return new TodoService(todoRepository);
    }

    @Bean
    UserService userService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return new UserService(userRepository, passwordEncoder);
    }

    @Bean
    public AuthenticationService authenticationService(UserService userService, PasswordEncoder passwordEncoder) {
        return new AuthenticationService(userService, passwordEncoder);
    }
}
