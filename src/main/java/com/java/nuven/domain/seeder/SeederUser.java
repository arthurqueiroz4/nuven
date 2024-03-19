package com.java.nuven.domain.seeder;

import com.java.nuven.application.dto.Params;
import com.java.nuven.domain.entity.User;
import com.java.nuven.domain.service.UserService;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SeederUser {

    private final UserService userService;

    public SeederUser(UserService userService) {
        this.userService = userService;
    }

    @EventListener
    public void seeder(ContextRefreshedEvent contextRefreshedEvent) {
        if (userService.findAll(new Params(0, 10, null)).isEmpty()) {
            User user = User.builder()
                    .username("admin")
                    .password("admin")
                    .role(User.Role.ADMIN)
                    .build();
            userService.save(user);
        }
    }
}
