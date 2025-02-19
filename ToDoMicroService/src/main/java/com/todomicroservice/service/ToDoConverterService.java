package com.todomicroservice.service;

import com.todomicroservice.dto.request.ToDoRequestDto;
import com.todomicroservice.dto.response.ToDoResponseDto;
import com.todomicroservice.entity.ToDo;
import org.springframework.stereotype.Service;


@Service
public class ToDoConverterService {
    public ToDo convertToToDo(ToDoRequestDto toDoRequestDto) {
        ToDo toDo = new ToDo();
        toDo.setTitle(toDoRequestDto.getTitle());
        toDo.setCompleted(toDoRequestDto.isCompleted());
        return toDo;
    }
    public ToDoResponseDto convertToResponse(ToDo todo) {
        ToDoResponseDto response = new ToDoResponseDto();
        response.setId(todo.getId());
        response.setTitle(todo.getTitle());
        response.setCompleted(todo.isCompleted());
        return response;
    }
}