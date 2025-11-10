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

    public void createToDo(String username, Long listId, ToDoRequestDto dto) {
        ToDoList toDoList = toDoListService.getAuthorizedListById(listId, username);

        ToDo todo = toDoConverterService.convertToToDo(username, dto);
        todo.setToDoList(toDoList);

        if (dto.getIntervalDays() != null && dto.getIntervalDays() > 0) {
            todo.setLastDoneDate(LocalDate.now());
            todo.setNextDueDate(LocalDate.now().plusDays(dto.getIntervalDays()));
        }

        toDoRepository.save(todo);
    }

    public List<ToDoResponseDto> getAllToDos(String username, Long listId) {
        ToDoList toDoList = toDoListService.getAuthorizedListById(listId, username);
        return toDoList.getToDos().stream()
                .map(toDoConverterService::convertToResponse)
                .collect(Collectors.toList());
    }

    public ToDoResponseDto updateToDo(String username, Long listId, Long toDoId, ToDoRequestDto dto) {
        ToDoList toDoList = toDoListService.getAuthorizedListById(listId, username);

        ToDo todo = toDoRepository.findById(toDoId)
                .filter(t -> t.getToDoList().getId().equals(toDoList.getId()))
                .orElseThrow(() -> new RuntimeException("ToDo not found or access denied"));

        todo.setTitle(dto.getTitle());
        todo.setCompleted(dto.isCompleted());
        todo.setNotifyUser(dto.isNotifyUser());
        todo.setIntervalDays(dto.getIntervalDays());
        todo.setLastDoneDate(dto.getLastDoneDate());

        if (dto.isCompleted()) {
            LocalDate doneDate = LocalDate.now();
            todo.setLastDoneDate(doneDate);

            if (todo.getIntervalDays() != null && todo.getIntervalDays() > 0) {
                todo.setNextDueDate(doneDate.plusDays(todo.getIntervalDays()));
            } else {
                todo.setNextDueDate(null);
            }
        }

        toDoRepository.save(todo);
        return toDoConverterService.convertToResponse(todo);
    }

    public void deleteToDo(String username, Long listId, Long toDoId) {
        ToDoList toDoList = toDoListService.getAuthorizedListById(listId, username);

        ToDo toDo = toDoRepository.findById(toDoId)
                .filter(t -> t.getToDoList().getId().equals(toDoList.getId()))
                .orElseThrow(() -> new RuntimeException("ToDo not found or access denied"));

        toDoRepository.delete(toDo);
    }
}

