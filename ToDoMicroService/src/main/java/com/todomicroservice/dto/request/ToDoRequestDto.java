package com.todomicroservice.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ToDoRequestDto {
    private String title;
    private boolean completed;
    private LocalDate startDate;
    private LocalDate lastDoneDate;
    private Integer intervalDays;
    private LocalDate nextDueDate;
    private boolean notifyUser;
}
