package com.todomicroservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ToDo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String title;
    private boolean completed;
    private LocalDate createdDate;
    private LocalDate updatedDate;
    @ManyToOne
    @JoinColumn(name = "todo_list_id")
    private ToDoList toDoList;
}
