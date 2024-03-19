package com.java.nuven.domain.repository;

import com.java.nuven.application.dto.Params;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface BaseRepository<T> {
    Optional<T> findById(UUID id);
    T save(T entity);
    Page<T> findAll(Params params);
}
