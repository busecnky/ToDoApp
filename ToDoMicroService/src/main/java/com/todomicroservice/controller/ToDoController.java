package com.todomicroservice.controller;

import com.todomicroservice.dto.request.ToDoRequestDto;
import com.todomicroservice.dto.response.ToDoResponseDto;
import com.todomicroservice.entity.ToDo;
import com.todomicroservice.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class ToDoController {
    private final ToDoService toDoService;

    public ToDoController(ToDoService toDoService) {
        this.toDoService = toDoService;
    }

    @GetMapping
    public ResponseEntity<List<ToDo>> getAllTodos() {
        return ResponseEntity.ok(toDoService.getAllToDos());
    }

    @PostMapping
    public ResponseEntity<ToDo> createTodo(@RequestBody ToDo todo) {
        return ResponseEntity.ok(toDoService.createToDo(todo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        toDoService.deleteToDo(id);
        return ResponseEntity.noContent().build();
    }

}
