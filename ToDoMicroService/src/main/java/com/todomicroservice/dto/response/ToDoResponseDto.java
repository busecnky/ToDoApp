package com.todomicroservice.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ToDoResponseDto {
    private Long id;
    private String title;
    private boolean completed;
    private String username;
    private LocalDate createdDate;
    private LocalDate lastDoneDate;
    private Integer intervalDays;
    private LocalDate nextDueDate;
    private boolean notifyUser;
}
