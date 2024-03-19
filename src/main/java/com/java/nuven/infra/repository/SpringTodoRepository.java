package com.java.nuven.infra.repository;

import com.java.nuven.domain.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface SpringTodoRepository extends JpaRepository<Todo, UUID>, JpaSpecificationExecutor<Todo> {
    Optional<Todo> findByIdAndDeletedAtIsNull(UUID id);
}
