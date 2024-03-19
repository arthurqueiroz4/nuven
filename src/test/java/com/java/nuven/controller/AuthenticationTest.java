package com.java.nuven.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.nuven.application.dto.LoginDTO;
import com.java.nuven.domain.exception.ErrorCode;
import com.java.nuven.domain.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class AuthenticationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    ObjectMapper objectMapper;

    String token;

    @BeforeEach
    void setUp() {
        token = "Bearer " + authenticationService.authenticate(new LoginDTO("admin", "admin")).getToken();
    }

    @Test
    void shouldReturnOKWhenAuthenticateWithValidCredentials() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth")
                        .header("Content-Type", "application/json")
                        .content(objectMapper.writeValueAsString(new LoginDTO("admin", "admin"))))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void shouldReturnUnauthorizedWhenAuthenticateWithInvalidCredentials() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth")
                        .header("Content-Type", "application/json")
                        .content(objectMapper.writeValueAsString(new LoginDTO("admin", "123"))))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title").value(ErrorCode.INVALID_CREDENTIALS.toString()))
                    .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.toString()));
    }

    @Test
    void shouldReturnUnauthorizedWhenUnloggedUserTryToGetTodos() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/todos"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnOKWhenLoggedUserTryToGetTodos() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/todos")
                        .header("Authorization", token))
                .andExpect(status().isOk());
    }
}
