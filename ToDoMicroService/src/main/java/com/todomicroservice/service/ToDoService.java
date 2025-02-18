package com.todomicroservice.service;

import com.todomicroservice.dto.request.ToDoRequestDto;
import com.todomicroservice.dto.response.ToDoResponseDto;
import com.todomicroservice.entity.ToDo;
import com.todomicroservice.repository.ToDoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ToDoService {
    private final ToDoRepository toDoRepository;
    private final ToDoConverterService toDoConverterService;

    public ToDoService(ToDoRepository toDoRepository, ToDoConverterService toDoConverterService) {
        this.toDoRepository = toDoRepository;
        this.toDoConverterService = toDoConverterService;
    }

    public List<ToDo> getAllToDos() {
        return toDoRepository.findAll();
    }

    public void createToDo(ToDoRequestDto toDoRequestDto) {
        toDoRepository.save(toDoConverterService.convertToToDo(toDoRequestDto));
        return ;
    }
    public ToDoResponseDto updateToDo(Long id, ToDoRequestDto request) {
        Optional<ToDo> optionalToDo = toDoRepository.findById(id);
        if (optionalToDo.isPresent()) {
            ToDo toDo = optionalToDo.get();
            toDo.setTitle(request.getTitle());
            toDo.setCompleted(request.isCompleted());
            toDoRepository.save(toDo);
            return toDoConverterService.convertToResponse(toDo);
        }
        throw new RuntimeException("ToDo not found");
    }
    public void deleteToDo(Long id) {
        toDoRepository.deleteById(id);
    }
}
