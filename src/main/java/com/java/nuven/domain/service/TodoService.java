package com.java.nuven.domain.service;

import com.java.nuven.application.dto.Params;
import com.java.nuven.application.dto.TodoDTO;
import com.java.nuven.domain.entity.Todo;
import com.java.nuven.domain.expection.DomainException;
import com.java.nuven.domain.expection.ErrorCode;
import com.java.nuven.domain.repository.TodoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class TodoService {
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public Page<Todo> findAll(Params params) {
        return todoRepository.findAll(params);
    }

    public TodoDTO getById(UUID id) {
        Todo todo = todoRepository.findById(id).orElseThrow(
                () -> new DomainException(ErrorCode.TODO_NOT_FOUND, HttpStatus.BAD_REQUEST));
        return new TodoDTO(todo);
    }

    public TodoDTO save(TodoDTO todoDTO) {
        Todo entity = new Todo();
        BeanUtils.copyProperties(todoDTO, entity);
        entity = todoRepository.save(entity);
        todoDTO.setId(entity.getId());
        return todoDTO;
    }

    public void delete(UUID id) {
        Todo todo = findById(id);
        todo.setDeletedAt(LocalDateTime.now());
        todoRepository.save(todo);
    }

    private Todo findById(UUID id) {
        return todoRepository.findById(id).orElseThrow(
                () -> new DomainException(ErrorCode.TODO_NOT_FOUND, HttpStatus.BAD_REQUEST));
    }

    public TodoDTO update(UUID id, TodoDTO todoDTO) {
        Todo todo = findById(id);
        BeanUtils.copyProperties(todoDTO, todo, "id");
        todoRepository.save(todo);
        return todoDTO;
    }
}
