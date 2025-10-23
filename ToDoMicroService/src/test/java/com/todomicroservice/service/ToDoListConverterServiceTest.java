package com.todomicroservice.service;

import com.todomicroservice.dto.request.ToDoListCreateRequestDto;
import com.todomicroservice.entity.ToDoList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ToDoListConverterServiceTest {

    private ToDoListConverterService converterService;

    @BeforeEach
    void setUp() {
        converterService = new ToDoListConverterService();
    }

    @Test
    void convertToToDoList_ShouldSetGivenTitleAndDate() {
        ToDoListCreateRequestDto dto = new ToDoListCreateRequestDto();
        dto.setTitle("List");
        dto.setDate(LocalDate.of(2025, 10, 23));

        ToDoList result = converterService.convertToToDoList(dto);

        assertNotNull(result);
        assertEquals("List", result.getTitle());
        assertEquals(LocalDate.of(2025, 10, 23), result.getDate());
    }

    @Test
    void convertToToDoList_ShouldUseCurrentDateIfDateIsNull() {
        ToDoListCreateRequestDto dto = new ToDoListCreateRequestDto();
        dto.setTitle("List");
        dto.setDate(null);

        ToDoList result = converterService.convertToToDoList(dto);

        assertNotNull(result);
        assertEquals("List", result.getTitle());
        assertEquals(LocalDate.now(), result.getDate());
    }
}

