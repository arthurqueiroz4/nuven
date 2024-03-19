package com.java.nuven.application.rest;

import com.java.nuven.application.dto.Params;
import com.java.nuven.application.dto.TodoDTO;
import com.java.nuven.domain.entity.Todo;
import com.java.nuven.domain.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public ResponseEntity<Page<Todo>> getAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                             @RequestParam(value = "size", defaultValue = "10") Integer size,
                                             @RequestParam(value = "title", required = false) String title,
                                             @RequestParam(value = "description", required = false) String description,
                                             @RequestParam(value = "date", required = false) String date) {
        Params params = new Params();
        params.setPage(page);
        params.setSize(size);
        Map<String, Object> map = new HashMap<>();
        map.put("title", title);
        map.put("description", description);
        map.put("date", date);
        params.setFilters(map);
        return ResponseEntity.ok(todoService.findAll(params));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(todoService.getById(id));
    }

    @PostMapping
    public ResponseEntity<TodoDTO> save(@Valid @RequestBody TodoDTO todoDTO) {
        return new ResponseEntity<>(todoService.save(todoDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        todoService.delete(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoDTO> update(@PathVariable UUID id, @Valid @RequestBody TodoDTO todoDTO) {
        return ResponseEntity.ok(todoService.update(id, todoDTO));
    }
}
