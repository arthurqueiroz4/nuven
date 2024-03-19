package com.java.nuven.infra.repository;

import com.java.nuven.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringUserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);

}
