package com.todomicroservice.controller;

import com.todomicroservice.dto.request.ToDoListCreateRequestDto;
import com.todomicroservice.service.ToDoListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/api/todolists")
@RequiredArgsConstructor
public class TodoListController {

    private final ToDoListService toDoListService;

    @PostMapping
    public ResponseEntity<?> createList(Authentication authentication,
                                        @RequestBody ToDoListCreateRequestDto dto) {
        String username = authentication.getName();
        return ResponseEntity.ok(toDoListService.createList(username, dto));
    }

    @GetMapping
    public ResponseEntity<?> getMyListsTitle(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(toDoListService.getMyListsTitle(username));
    }

    @PostMapping("/{listId}/members")
    public ResponseEntity<?> addMember(@PathVariable Long listId,
                                       @RequestParam String usernameToAdd,
                                       Authentication authentication) throws AccessDeniedException {
        String currentUser = authentication.getName();
        toDoListService.addMember(listId, usernameToAdd, currentUser);
        return ResponseEntity.ok().build();
    }
}

