package com.todomicroservice.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ToDoRequestDto {
    private String title;
    private boolean completed;
}
