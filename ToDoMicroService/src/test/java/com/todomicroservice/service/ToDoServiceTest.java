package com.todomicroservice.service;

import com.todomicroservice.dto.request.ToDoRequestDto;
import com.todomicroservice.dto.response.ToDoResponseDto;
import com.todomicroservice.entity.ToDo;
import com.todomicroservice.entity.ToDoList;
import com.todomicroservice.repository.ToDoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ToDoServiceTest {

    private ToDoRepository toDoRepository;
    private ToDoConverterService toDoConverterService;
    private ToDoListService toDoListService;
    private ToDoService toDoService;

    private final String USERNAME = "testUser";
    private final Long LIST_ID = 1L;
    private final Long TODO_ID = 100L;

    private ToDoList toDoList;
    private ToDo toDo;
    private ToDoRequestDto requestDto;
    private ToDoResponseDto responseDto;

    @BeforeEach
    void setUp() {
        toDoRepository = mock(ToDoRepository.class);
        toDoConverterService = mock(ToDoConverterService.class);
        toDoListService = mock(ToDoListService.class);

        toDoService = new ToDoService(toDoRepository, toDoConverterService, toDoListService);

        toDoList = new ToDoList();
        toDoList.setId(LIST_ID);

        toDo = new ToDo();
        toDo.setId(TODO_ID);
        toDo.setTitle("Sample");
        toDo.setToDoList(toDoList);

        requestDto = new ToDoRequestDto();
        requestDto.setTitle("Updated Title");
        requestDto.setCompleted(true);
        requestDto.setNotifyUser(true);
        requestDto.setIntervalDays(10);
        requestDto.setLastDoneDate(LocalDate.of(2025, 9, 2));

        responseDto = new ToDoResponseDto();
        responseDto.setId(TODO_ID);
        responseDto.setTitle("Updated Title");
    }

    @Test
    void createToDo_ShouldSaveToRepository_WithNextDueDate() {
        when(toDoListService.getAuthorizedListById(LIST_ID, USERNAME)).thenReturn(toDoList);
        when(toDoConverterService.convertToToDo(USERNAME, requestDto)).thenReturn(toDo);

        toDoService.createToDo(USERNAME, LIST_ID, requestDto);

        ArgumentCaptor<ToDo> captor = ArgumentCaptor.forClass(ToDo.class);
        verify(toDoRepository).save(captor.capture());

        ToDo saved = captor.getValue();
        assertEquals(LIST_ID, saved.getToDoList().getId());
        assertNotNull(saved.getLastDoneDate());
        assertEquals(saved.getNextDueDate(), saved.getLastDoneDate().plusDays(requestDto.getIntervalDays()));
    }

    @Test
    void createToDo_ShouldNotSetNextDueDate_WhenIntervalNull() {
        requestDto.setIntervalDays(null);
        when(toDoListService.getAuthorizedListById(LIST_ID, USERNAME)).thenReturn(toDoList);
        when(toDoConverterService.convertToToDo(USERNAME, requestDto)).thenReturn(toDo);

        toDoService.createToDo(USERNAME, LIST_ID, requestDto);

        ArgumentCaptor<ToDo> captor = ArgumentCaptor.forClass(ToDo.class);
        verify(toDoRepository).save(captor.capture());

        ToDo saved = captor.getValue();
        assertNull(saved.getNextDueDate());
    }

    @Test
    void getAllToDos_ShouldReturnListOfResponses() {
        toDoList.setToDos(Collections.singletonList(toDo));
        when(toDoListService.getAuthorizedListById(LIST_ID, USERNAME)).thenReturn(toDoList);
        when(toDoConverterService.convertToResponse(toDo)).thenReturn(responseDto);

        List<ToDoResponseDto> result = toDoService.getAllToDos(USERNAME, LIST_ID);

        assertEquals(1, result.size());
        assertEquals("Updated Title", result.get(0).getTitle());
    }

    @Test
    void updateToDo_ShouldUpdateAndReturnResponse_WithNextDueDate() {
        when(toDoListService.getAuthorizedListById(LIST_ID, USERNAME)).thenReturn(toDoList);
        when(toDoRepository.findById(TODO_ID)).thenReturn(Optional.of(toDo));
        when(toDoConverterService.convertToResponse(toDo)).thenReturn(responseDto);

        ToDoResponseDto result = toDoService.updateToDo(USERNAME, LIST_ID, TODO_ID, requestDto);

        assertEquals("Updated Title", result.getTitle());
        assertTrue(toDo.isCompleted());
        assertEquals(requestDto.getIntervalDays(), toDo.getIntervalDays());
        assertNotNull(toDo.getLastDoneDate());
        assertEquals(toDo.getNextDueDate(), toDo.getLastDoneDate().plusDays(requestDto.getIntervalDays()));
        verify(toDoRepository).save(toDo);
    }

    @Test
    void updateToDo_ShouldClearNextDueDate_WhenNoInterval() {
        requestDto.setCompleted(true);
        requestDto.setIntervalDays(null);
        when(toDoListService.getAuthorizedListById(LIST_ID, USERNAME)).thenReturn(toDoList);
        when(toDoRepository.findById(TODO_ID)).thenReturn(Optional.of(toDo));
        when(toDoConverterService.convertToResponse(toDo)).thenReturn(responseDto);

        toDoService.updateToDo(USERNAME, LIST_ID, TODO_ID, requestDto);

        assertNull(toDo.getNextDueDate());
    }

    @Test
    void updateToDo_ShouldThrow_WhenNotFound() {
        when(toDoListService.getAuthorizedListById(LIST_ID, USERNAME)).thenReturn(toDoList);
        when(toDoRepository.findById(TODO_ID)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> toDoService.updateToDo(USERNAME, LIST_ID, TODO_ID, requestDto));
    }

    @Test
    void deleteToDo_ShouldDeleteFromRepository() {
        when(toDoListService.getAuthorizedListById(LIST_ID, USERNAME)).thenReturn(toDoList);
        when(toDoRepository.findById(TODO_ID)).thenReturn(Optional.of(toDo));

        toDoService.deleteToDo(USERNAME, LIST_ID, TODO_ID);

        verify(toDoRepository).delete(toDo);
    }

    @Test
    void deleteToDo_ShouldThrow_WhenNotFound() {
        when(toDoListService.getAuthorizedListById(LIST_ID, USERNAME)).thenReturn(toDoList);
        when(toDoRepository.findById(TODO_ID)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> toDoService.deleteToDo(USERNAME, LIST_ID, TODO_ID));
    }
}
