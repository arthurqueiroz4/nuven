package com.java.nuven.domain.repository;

import com.java.nuven.domain.entity.User;

import java.util.Optional;

public interface UserRepository extends BaseRepository<User> {

    Optional<User> findByUsername(String username);
}
