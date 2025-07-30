package com.todomicroservice.dto.request;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class ToDoListCreateRequestDto {
    private String title;
    private LocalDate date;
}
