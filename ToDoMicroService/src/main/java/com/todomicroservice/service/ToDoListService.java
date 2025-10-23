package com.todomicroservice.service;

import com.todomicroservice.dto.request.ToDoListCreateRequestDto;
import com.todomicroservice.entity.ToDoList;
import com.todomicroservice.repository.ToDoListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ToDoListService {

    private final ToDoListRepository toDoListRepository;
    private final ToDoListConverterService toDoListConverterService;

    public ToDoList createList(String username, ToDoListCreateRequestDto toDoListCreateRequestDto) {
        ToDoList list = toDoListConverterService.convertToToDoList(toDoListCreateRequestDto);
        list.getMembers().add(username);
        return toDoListRepository.save(list);
    }

    public List<String> getMyListsTitle(String username) {
        return toDoListRepository.findTitlesByMember(username);
    }

    public void addMember(Long listId, String memberUsername, String currentUsername) throws AccessDeniedException {
        ToDoList list = toDoListRepository.findById(listId)
                .orElseThrow(() -> new RuntimeException("List not found"));

        if (!list.getMembers().contains(currentUsername)) {
            throw new AccessDeniedException("You are not allowed to add members to this list");
        }

        list.getMembers().add(memberUsername);
        toDoListRepository.save(list);
    }
    public ToDoList getAuthorizedListById(Long listId, String username) {
        return toDoListRepository.findById(listId)
                .filter(list -> list.getMembers().contains(username))
                .orElseThrow(() -> new RuntimeException("List not found or access denied"));
    }
}