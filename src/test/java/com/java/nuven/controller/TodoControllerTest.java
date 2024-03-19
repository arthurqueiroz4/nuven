package com.java.nuven.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.nuventest.application.dto.TodoDTO;
import com.java.nuventest.domain.exception.DomainException;
import com.java.nuventest.domain.exception.ErrorCode;
import com.java.nuventest.domain.service.TodoService;
import com.java.nuventest.infra.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class TodoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TodoService todoService;

    @Autowired
    ObjectMapper objectMapper;

    String token;

    @BeforeEach
    void setUp() {
        token = "Bearer " + new JwtUtils().generateToken("admin");
    }

    @Test
    void shouldReturnOKWhenCreateTodoInPost() throws Exception {
        TodoDTO todoDTO = TodoDTO.builder()
                .title("title")
                .description("description")
                .dueDate(LocalDateTime.now().plusDays(2))
                .build();

        given(todoService.save(ArgumentMatchers.any())).willReturn(todoDTO);

        ResultActions resultActions = mockMvc.perform(post("/api/v1/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .content(objectMapper.writeValueAsString(todoDTO)));

        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.description").value("description"))
                .andExpect(jsonPath("$.dueDate").exists())
                .andReturn();
    }

    @Test
    void shouldReturnBadRequestWhenTitleIsNullInPost() throws Exception {
        TodoDTO todoDTO = TodoDTO.builder()
                .description("description")
                .dueDate(LocalDateTime.now().plusDays(2))
                .build();

        mockMvc.perform(post("/api/v1/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .content(objectMapper.writeValueAsString(todoDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Validation Error"))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.toString()))
                .andExpect(jsonPath("$.detail").isArray())
                .andExpect(jsonPath("$.detail[0].field").value("title"));
    }

    @Test
    void shouldReturnBadRequestWhenDescriptionIsNullInPost() throws Exception {
        TodoDTO todoDTO = TodoDTO.builder()
                .title("title")
                .dueDate(LocalDateTime.now().plusDays(2))
                .build();

        mockMvc.perform(post("/api/v1/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .content(objectMapper.writeValueAsString(todoDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Validation Error"))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.toString()))
                .andExpect(jsonPath("$.detail").isArray())
                .andExpect(jsonPath("$.detail[0].field").value("description"));
    }

    @Test
    void shouldReturnBadRequestWhenDueDateIsNullOrIsPastInPost() throws Exception {
        TodoDTO todoDTO = TodoDTO.builder()
                .title("title")
                .description("description")
                .build();

        mockMvc.perform(post("/api/v1/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .content(objectMapper.writeValueAsString(todoDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Validation Error"))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.toString()))
                .andExpect(jsonPath("$.detail").isArray())
                .andExpect(jsonPath("$.detail[0].field").value("dueDate"));

        todoDTO = TodoDTO.builder()
                .title("title")
                .description("description")
                .dueDate(LocalDateTime.now().minusDays(2))
                .build();

        mockMvc.perform(post("/api/v1/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .content(objectMapper.writeValueAsString(todoDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Validation Error"))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.toString()))
                .andExpect(jsonPath("$.detail").isArray())
                .andExpect(jsonPath("$.detail[0].field").value("dueDate"));
    }

    @Test
    void shouldReturnNoContentWhenTodoIsDeleted() throws Exception {
        mockMvc.perform(delete("/api/v1/todos/" + UUID.randomUUID())
                .header("Authorization", token))
                .andExpect(status().isNoContent());
        verify(todoService, times(1)).delete(ArgumentMatchers.any());
    }

    @Test
    void shouldReturnBadRequestWhenIdIsInvalidInDelete() throws Exception {
        mockMvc.perform(delete("/api/v1/todos/invalid-id")
                .header("Authorization", token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Parse Error"))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.toString()))
                .andExpect(jsonPath("$.detail").value("The parameter id must be of type UUID"));
    }

    @Test
    void shouldReturnBadRequestWhenIdIsNotFoundInDelete() throws Exception {
        UUID id = UUID.randomUUID();
        doThrow(new DomainException(ErrorCode.TODO_NOT_FOUND, HttpStatus.BAD_REQUEST)).when(todoService).delete(id);
        mockMvc.perform(delete("/api/v1/todos/" + id)
                .header("Authorization", token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value(ErrorCode.TODO_NOT_FOUND.toString()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.toString()))
                .andExpect(jsonPath("$.detail").value(ErrorCode.TODO_NOT_FOUND.toString()));
    }

    @Test
    void shouldReturnOKWhenTodoIsUpdated() throws Exception {
        UUID id = UUID.randomUUID();
        TodoDTO todoDTO = TodoDTO.builder()
                .title("title")
                .description("description")
                .dueDate(LocalDateTime.now().plusDays(2))
                .build();

        when(todoService.update(any(), any())).thenReturn(todoDTO);

        ResultActions resultActions = mockMvc.perform(put("/api/v1/todos/" + id)
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todoDTO)));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.description").value("description"))
                .andExpect(jsonPath("$.dueDate").exists())
                .andReturn();
    }

    @Test
    void shouldReturnBadRequestWhenIdIsInvalidInUpdate() throws Exception {
        TodoDTO todoDTO = TodoDTO.builder()
                .title("title")
                .description("description")
                .dueDate(LocalDateTime.now().plusDays(2))
                .build();

        mockMvc.perform(put("/api/v1/todos/invalid-id")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .content(objectMapper.writeValueAsString(todoDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnOKWhenGetTodoById() throws Exception {
        UUID id = UUID.randomUUID();
        TodoDTO todoDTO = TodoDTO.builder()
                .id(id)
                .title("title")
                .description("description")
                .dueDate(LocalDateTime.now().plusDays(2))
                .build();

        given(todoService.getById(id)).willReturn(todoDTO);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/todos/" + id)
                .header("Authorization", token));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.description").value("description"))
                .andExpect(jsonPath("$.dueDate").exists())
                .andReturn();
    }

    @Test
    void shouldReturnBadRequestWhenIdIsInvalidInGetById() throws Exception {
        mockMvc.perform(get("/api/v1/todos/invalid-id")
                        .header("Authorization", token))
                        .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnOKWhenGetAllTodos() throws Exception {
        when(todoService.findAll(ArgumentMatchers.any())).thenReturn(Page.empty(PageRequest.of(0, 10)));
        mockMvc.perform(get("/api/v1/todos").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.pageable.pageSize").value(10));
    }
}
