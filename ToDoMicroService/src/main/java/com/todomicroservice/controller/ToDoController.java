package com.todomicroservice.controller;

import com.todomicroservice.dto.request.ToDoRequestDto;
import com.todomicroservice.dto.response.ToDoResponseDto;
import com.todomicroservice.service.ToDoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todolists/{listId}/todos")
public class ToDoController {

    private final ToDoService toDoService;

    public ToDoController(ToDoService toDoService) {
        this.toDoService = toDoService;
    }

    @PostMapping
    public ResponseEntity<Void> createToDo(@PathVariable Long listId,
                                           @RequestBody ToDoRequestDto toDoRequestDto,
                                           Authentication authentication) {
        String username = authentication.getName();
        toDoService.createToDo(username, listId, toDoRequestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ToDoResponseDto>> getAllToDos(@PathVariable Long listId,
                                                             Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(toDoService.getAllToDos(username, listId));
    }

    @PutMapping("/{todoId}")
    public ResponseEntity<ToDoResponseDto> updateToDo(@PathVariable Long listId,
                                                      @PathVariable Long todoId,
                                                      @RequestBody ToDoRequestDto request,
                                                      Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(toDoService.updateToDo(username, listId, todoId, request));
    }

    @DeleteMapping("/{todoId}")
    public ResponseEntity<Void> deleteToDo(@PathVariable Long listId,
                                           @PathVariable Long toDoId,
                                           Authentication authentication) {
        String username = authentication.getName();
        toDoService.deleteToDo(username, listId, toDoId);
        return ResponseEntity.noContent().build();
    }
}