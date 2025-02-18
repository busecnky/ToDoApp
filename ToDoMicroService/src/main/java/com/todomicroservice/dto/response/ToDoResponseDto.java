package com.todomicroservice.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ToDoResponseDto {
    private Long id;
    private String title;
    private boolean completed;
    private String username;

}
