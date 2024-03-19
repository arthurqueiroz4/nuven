package com.java.nuven.infra.init;

import com.java.nuven.domain.repository.TodoRepository;
import com.java.nuven.domain.service.TodoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    TodoService todoService(TodoRepository todoRepository) {
        return new TodoService(todoRepository);
    }
}
