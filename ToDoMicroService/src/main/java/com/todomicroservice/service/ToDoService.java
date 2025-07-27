package com.todomicroservice.service;

import com.todomicroservice.dto.request.ToDoRequestDto;
import com.todomicroservice.dto.response.ToDoResponseDto;
import com.todomicroservice.entity.ToDo;
import com.todomicroservice.repository.ToDoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToDoService {
    private final ToDoRepository toDoRepository;
    private final ToDoConverterService toDoConverterService;

    public ToDoService(ToDoRepository toDoRepository, ToDoConverterService toDoConverterService) {
        this.toDoRepository = toDoRepository;
        this.toDoConverterService = toDoConverterService;
    }

    public List<ToDo> getAllToDos(String username) {
        return toDoRepository.findByUsername(username);
    }

    public void createToDo(String username, ToDoRequestDto toDoRequestDto) {
        toDoRepository.save(toDoConverterService.convertToToDo(username, toDoRequestDto));
    }

    public ToDoResponseDto updateToDo(String username, Long id, ToDoRequestDto request) {
        ToDo toDo = toDoRepository.findByIdAndUsername(id, username)
                .orElseThrow(() -> new RuntimeException("ToDo not found or access denied"));
        toDo.setTitle(request.getTitle());
        toDo.setCompleted(request.isCompleted());
        toDoRepository.save(toDo);
        return toDoConverterService.convertToResponse(toDo);
    }

    public void deleteToDo(String username, Long id) {
        ToDo toDo = toDoRepository.findByIdAndUsername(id, username)
                .orElseThrow(() -> new RuntimeException("ToDo not found or access denied"));
        toDoRepository.delete(toDo);
    }
}

