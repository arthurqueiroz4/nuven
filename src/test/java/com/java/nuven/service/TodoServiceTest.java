package com.java.nuven.service;

import com.java.nuven.application.dto.TodoDTO;
import com.java.nuven.domain.entity.Todo;
import com.java.nuven.domain.repository.TodoRepository;
import com.java.nuven.domain.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {

    @MockBean
    TodoRepository todoRepository;

    TodoService todoService;

    @BeforeEach
    void setUp() {
        todoRepository = mock(TodoRepository.class);
        todoService = new TodoService(todoRepository);
    }

    @Test
    void shouldReturnTodoInCreate() {
        TodoDTO todoDTO = TodoDTO.builder()
                .title("title")
                .description("description")
                .dueDate(LocalDateTime.now())
                .build();
        UUID id = UUID.randomUUID();
        Todo todo = Todo.builder()
                .id(id)
                .title("title")
                .description("description")
                .dueDate(LocalDateTime.now())
                .build();


        when(todoRepository.save(ArgumentMatchers.any())).thenReturn(todo);

        TodoDTO todoDTOResponse = todoService.save(todoDTO);

        verify(todoRepository, times(1)).save(ArgumentMatchers.any());
        assertNotNull(todoDTOResponse.getId());
        assertEquals(todoDTO.getTitle(), todoDTOResponse.getTitle());
        assertEquals(todoDTO.getDescription(), todoDTOResponse.getDescription());
        assertEquals(todoDTO.getDueDate(), todoDTOResponse.getDueDate());
    }

    @Test
    void shouldReturnTodoInUpdate() {
        UUID id = UUID.randomUUID();
        TodoDTO todoDTO = TodoDTO.builder()
                .title("new title")
                .description("new description")
                .dueDate(LocalDateTime.now())
                .build();
        Todo todoOld = Todo.builder()
                .id(id)
                .title("title")
                .description("description")
                .dueDate(LocalDateTime.now())
                .build();
        Todo todoNew = Todo.builder()
                .id(id)
                .title("new title")
                .description("new description")
                .dueDate(LocalDateTime.now())
                .build();

        when(todoRepository.findById(id)).thenReturn(Optional.of(todoOld));
        when(todoRepository.save(ArgumentMatchers.any())).thenReturn(todoNew);

        TodoDTO todoDTOResponse = todoService.update(id, todoDTO);

        verify(todoRepository, times(1)).findById(id);
        verify(todoRepository, times(1)).save(ArgumentMatchers.any());
        assertEquals(todoDTO.getTitle(), todoDTOResponse.getTitle());
        assertEquals(todoDTO.getDescription(), todoDTOResponse.getDescription());
        assertEquals(todoDTO.getDueDate(), todoDTOResponse.getDueDate());
    }

    @Test
    void shouldReturnTodoInGetById() {
        UUID id = UUID.randomUUID();
        Todo todo = Todo.builder()
                .id(id)
                .title("title")
                .description("description")
                .dueDate(LocalDateTime.now())
                .build();

        when(todoRepository.findById(id)).thenReturn(Optional.of(todo));

        TodoDTO todoDTOResponse = todoService.getById(id);

        verify(todoRepository, times(1)).findById(id);
        assertEquals(todo.getId(), todoDTOResponse.getId());
        assertEquals(todo.getTitle(), todoDTOResponse.getTitle());
        assertEquals(todo.getDescription(), todoDTOResponse.getDescription());
        assertEquals(todo.getDueDate(), todoDTOResponse.getDueDate());
    }

    @Test
    void shouldReturnTodoInDelete() {
        UUID id = UUID.randomUUID();
        Todo todo = Todo.builder()
                .id(id)
                .title("title")
                .description("description")
                .dueDate(LocalDateTime.now())
                .build();

        when(todoRepository.findById(id)).thenReturn(Optional.of(todo));
        when(todoRepository.save(ArgumentMatchers.any())).thenReturn(todo);

        todoService.delete(id);

        verify(todoRepository, times(1)).findById(id);
        verify(todoRepository, times(1)).save(ArgumentMatchers.any());
    }

}

