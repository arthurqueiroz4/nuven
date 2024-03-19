package com.java.nuven.infra.repository;

import com.java.nuven.application.dto.Params;
import com.java.nuven.domain.entity.User;
import com.java.nuven.domain.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class PostgresUserRepository implements UserRepository {


    private final SpringUserRepository userRepository;

    public PostgresUserRepository(SpringUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public User save(User entity) {
        return userRepository.save(entity);
    }

    @Override
    public Page<User> findAll(Params params) {
        return userRepository.findAll(PageRequest.of(params.getPage(), params.getSize()));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
