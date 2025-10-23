package com.todomicroservice.service;

import com.todomicroservice.dto.request.ToDoRequestDto;
import com.todomicroservice.dto.response.ToDoResponseDto;
import com.todomicroservice.entity.ToDo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ToDoConverterServiceTest {

    private ToDoConverterService converterService;

    @BeforeEach
    void setUp() {
        converterService = new ToDoConverterService();
    }

    @Test
    void convertToToDo_ShouldMapAllFields_WhenDateIsProvided() {
        ToDoRequestDto dto = new ToDoRequestDto();
        dto.setTitle("List");
        dto.setCompleted(true);
        dto.setDate(LocalDate.of(2025, 10, 23));

        String username = "username";

        ToDo result = converterService.convertToToDo(username, dto);

        assertNotNull(result);
        assertEquals("username", result.getUsername());
        assertEquals("List", result.getTitle());
        assertTrue(result.isCompleted());
        assertEquals(LocalDate.of(2025, 10, 23), result.getCreatedDate());
    }

    @Test
    void convertToToDo_ShouldUseCurrentDate_WhenDateIsNull() {
        ToDoRequestDto dto = new ToDoRequestDto();
        dto.setTitle("List");
        dto.setCompleted(false);
        dto.setDate(null);

        String username = "username";

        ToDo result = converterService.convertToToDo(username, dto);

        assertNotNull(result);
        assertEquals("List", result.getTitle());
        assertEquals("username", result.getUsername());
        assertFalse(result.isCompleted());
        assertEquals(LocalDate.now(), result.getCreatedDate());
    }

    @Test
    void convertToResponse_ShouldMapAllFieldsCorrectly() {
        ToDo toDo = new ToDo();
        toDo.setId(10L);
        toDo.setTitle("List");
        toDo.setCompleted(true);
        toDo.setUsername("username");

        ToDoResponseDto response = converterService.convertToResponse(toDo);

        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals("List", response.getTitle());
        assertTrue(response.isCompleted());
        assertEquals("username", response.getUsername());
    }
}

