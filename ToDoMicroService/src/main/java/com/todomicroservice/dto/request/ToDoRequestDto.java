package com.todomicroservice.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ToDoRequestDto {
    private String title;
    private boolean completed;
    private LocalDate date;
}
