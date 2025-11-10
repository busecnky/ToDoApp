package com.todomicroservice.service;

import com.todomicroservice.dto.request.ToDoRequestDto;
import com.todomicroservice.dto.response.ToDoResponseDto;
import com.todomicroservice.entity.ToDo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class ToDoConverterServiceTest {

    private ToDoConverterService converterService;

    @BeforeEach
    void setUp() {
        converterService = new ToDoConverterService();
    }

    @Test
    void convertToToDo_shouldMapAllFieldsCorrectly_whenIntervalDaysPresent() {
        ToDoRequestDto dto = new ToDoRequestDto();
        dto.setTitle("Change car oil");
        dto.setCompleted(false);
        dto.setStartDate(LocalDate.of(2025, 1, 1));
        dto.setNotifyUser(true);
        dto.setIntervalDays(90);
        dto.setLastDoneDate(LocalDate.of(2025, 1, 1));

        ToDo toDo = converterService.convertToToDo("username", dto);

        assertThat(toDo.getUsername()).isEqualTo("username");
        assertThat(toDo.getTitle()).isEqualTo("Change car oil");
        assertThat(toDo.isCompleted()).isFalse();
        assertThat(toDo.isNotifyUser()).isTrue();
        assertThat(toDo.getIntervalDays()).isEqualTo(90);
        assertThat(toDo.getLastDoneDate()).isEqualTo(LocalDate.of(2025, 1, 1));
        assertThat(toDo.getNextDueDate()).isEqualTo(LocalDate.of(2025, 4, 1)); // +90 gün
    }

    @Test
    void convertToToDo_shouldHandleNullIntervalDaysGracefully() {
        ToDoRequestDto dto = new ToDoRequestDto();
        dto.setTitle("Dentist visit");
        dto.setCompleted(false);
        dto.setNotifyUser(false);
        dto.setIntervalDays(null);
        dto.setLastDoneDate(null);

        ToDo toDo = converterService.convertToToDo("username", dto);

        assertThat(toDo.getTitle()).isEqualTo("Dentist visit");
        assertThat(toDo.getCreatedDate()).isNotNull();
        assertThat(toDo.getNextDueDate()).isNull();
    }

    @Test
    void convertToResponse_shouldReturnMatchingDto() {
        ToDo toDo = new ToDo();
        toDo.setId(1L);
        toDo.setUsername("username");
        toDo.setTitle("Water plants");
        toDo.setCompleted(true);
        toDo.setCreatedDate(LocalDate.of(2025, 1, 1));
        toDo.setLastDoneDate(LocalDate.of(2025, 1, 2));
        toDo.setIntervalDays(3);
        toDo.setNextDueDate(LocalDate.of(2025, 1, 5));
        toDo.setNotifyUser(true);

        ToDoResponseDto response = converterService.convertToResponse(toDo);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getUsername()).isEqualTo("username");
        assertThat(response.getTitle()).isEqualTo("Water plants");
        assertThat(response.isCompleted()).isTrue();
        assertThat(response.getCreatedDate()).isEqualTo(LocalDate.of(2025, 1, 1));
        assertThat(response.getLastDoneDate()).isEqualTo(LocalDate.of(2025, 1, 2));
        assertThat(response.getNextDueDate()).isEqualTo(LocalDate.of(2025, 1, 5));
        assertThat(response.getIntervalDays()).isEqualTo(3);
        assertThat(response.isNotifyUser()).isTrue();
    }
}
