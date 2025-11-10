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
        toDo.setCreatedDate(toDoRequestDto.getStartDate() != null ? toDoRequestDto.getStartDate() : LocalDate.now());
        toDo.setNotifyUser(toDoRequestDto.isNotifyUser());
        toDo.setIntervalDays(toDoRequestDto.getIntervalDays());
        toDo.setLastDoneDate(toDoRequestDto.getLastDoneDate());

        if (toDoRequestDto.getIntervalDays() != null && toDoRequestDto.getIntervalDays() > 0) {
            LocalDate lastDate = toDoRequestDto.getLastDoneDate() != null ? toDoRequestDto.getLastDoneDate() : LocalDate.now();
            toDo.setNextDueDate(lastDate.plusDays(toDoRequestDto.getIntervalDays()));
        }

        return toDo;
    }

    public ToDoResponseDto convertToResponse(ToDo toDo) {
        ToDoResponseDto toDoResponseDto = new ToDoResponseDto();
        toDoResponseDto.setId(toDo.getId());
        toDoResponseDto.setTitle(toDo.getTitle());
        toDoResponseDto.setCompleted(toDo.isCompleted());
        toDoResponseDto.setUsername(toDo.getUsername());
        toDoResponseDto.setCreatedDate(toDo.getCreatedDate());
        toDoResponseDto.setLastDoneDate(toDo.getLastDoneDate());
        toDoResponseDto.setIntervalDays(toDo.getIntervalDays());
        toDoResponseDto.setNextDueDate(toDo.getNextDueDate());
        toDoResponseDto.setNotifyUser(toDo.isNotifyUser());
        return toDoResponseDto;
    }
}
