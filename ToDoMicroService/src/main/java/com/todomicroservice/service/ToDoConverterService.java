package com.todomicroservice.service;

import com.todomicroservice.dto.request.ToDoRequestDto;
import com.todomicroservice.dto.response.ToDoResponseDto;
import com.todomicroservice.entity.ToDo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
public class ToDoConverterService {
    public ToDo convertToToDo(String username, ToDoRequestDto toDoRequestDto) {
        ToDo toDo = new ToDo();
        toDo.setUsername(username);
        toDo.setTitle(toDoRequestDto.getTitle());
        toDo.setCompleted(toDoRequestDto.isCompleted());
        toDo.setCreatedDate(toDoRequestDto.getDate() != null ? toDoRequestDto.getDate() : LocalDate.now());
        return toDo;
    }
    public ToDoResponseDto convertToResponse(ToDo toDo) {
        ToDoResponseDto response = new ToDoResponseDto();
        response.setId(toDo.getId());
        response.setTitle(toDo.getTitle());
        response.setCompleted(toDo.isCompleted());
        response.setUsername(toDo.getUsername());
        return response;
    }
}