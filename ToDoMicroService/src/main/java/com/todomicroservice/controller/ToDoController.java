package com.todomicroservice.controller;

import com.todomicroservice.dto.request.ToDoRequestDto;
import com.todomicroservice.dto.response.ToDoResponseDto;
import com.todomicroservice.entity.ToDo;
import com.todomicroservice.service.ToDoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todo")
public class ToDoController {
    private final ToDoService toDoService;

    public ToDoController(ToDoService toDoService) {
        this.toDoService = toDoService;
    }

    @GetMapping
    public ResponseEntity<List<ToDo>> getAllTodos(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(toDoService.getAllToDos(username));
    }

    @PostMapping
    public ResponseEntity<Void> createTodo(Authentication authentication,
                                           @RequestBody ToDoRequestDto request) {
        String username = authentication.getName();
        toDoService.createToDo(username, request);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<ToDoResponseDto> updateTodo(Authentication authentication,
                                                      @PathVariable Long id,
                                                      @RequestBody ToDoRequestDto request) {
        String username = authentication.getName();
        return ResponseEntity.ok(toDoService.updateToDo(username, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(Authentication authentication,
                                           @PathVariable Long id) {
        String username = authentication.getName();
        toDoService.deleteToDo(username, id);
        return ResponseEntity.noContent().build();
    }
}
