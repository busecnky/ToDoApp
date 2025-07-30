package com.todomicroservice.service;

import com.todomicroservice.dto.request.ToDoRequestDto;
import com.todomicroservice.dto.response.ToDoResponseDto;
import com.todomicroservice.entity.ToDo;
import com.todomicroservice.entity.ToDoList;
import com.todomicroservice.repository.ToDoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ToDoService {
    private final ToDoRepository toDoRepository;
    private final ToDoConverterService toDoConverterService;
    private final ToDoListService toDoListService;


    public ToDoService(ToDoRepository toDoRepository, ToDoConverterService toDoConverterService, ToDoListService toDoListService) {
        this.toDoRepository = toDoRepository;
        this.toDoConverterService = toDoConverterService;
        this.toDoListService = toDoListService;
    }

    public void createToDo(String username, Long listId, ToDoRequestDto toDoRequestDto) {
        ToDoList toDoList = toDoListService.getAuthorizedListById(listId, username);

        ToDo toDo = toDoConverterService.convertToToDo(username, toDoRequestDto);
        toDo.setToDoList(toDoList);

        toDoRepository.save(toDo);
    }

    public List<ToDoResponseDto> getAllToDos(String username, Long listId) {
        ToDoList toDoList = toDoListService.getAuthorizedListById(listId, username);

        return toDoList.getToDos().stream()
                .map(toDoConverterService::convertToResponse)
                .collect(Collectors.toList());
    }

    public ToDoResponseDto updateToDo(String username, Long listId, Long toDoId, ToDoRequestDto toDoRequestDto) {
        ToDoList toDoList = toDoListService.getAuthorizedListById(listId, username);

        ToDo toDo = toDoRepository.findById(toDoId)
                .filter(t -> t.getToDoList().getId().equals(toDoList.getId()))
                .orElseThrow(() -> new RuntimeException("ToDo not found or access denied"));

        toDo.setTitle(toDoRequestDto.getTitle());
        toDo.setCompleted(toDoRequestDto.isCompleted());
        toDo.setUpdatedDate(toDoRequestDto.getDate());

        toDoRepository.save(toDo);
        return toDoConverterService.convertToResponse(toDo);
    }

    public void deleteToDo(String username, Long listId, Long toDoId) {
        ToDoList toDoList = toDoListService.getAuthorizedListById(listId, username);

        ToDo toDo = toDoRepository.findById(toDoId)
                .filter(t -> t.getToDoList().getId().equals(toDoList.getId()))
                .orElseThrow(() -> new RuntimeException("ToDo not found or access denied"));

        toDoRepository.delete(toDo);
    }
}

