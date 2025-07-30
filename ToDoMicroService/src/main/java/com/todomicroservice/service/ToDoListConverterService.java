package com.todomicroservice.service;

import com.todomicroservice.dto.request.ToDoListCreateRequestDto;
import com.todomicroservice.entity.ToDoList;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
public class ToDoListConverterService {
    public ToDoList convertToToDoList(ToDoListCreateRequestDto toDoListCreateRequestDto) {
        ToDoList toDoList = new ToDoList();
        toDoList.setTitle(toDoListCreateRequestDto.getTitle());
        toDoList.setDate(toDoListCreateRequestDto.getDate() != null ? toDoListCreateRequestDto.getDate() : LocalDate.now());
        return toDoList;
    }

}