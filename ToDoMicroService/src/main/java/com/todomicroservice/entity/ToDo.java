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
@Table(name = "todo", schema = "todos_service")
public class ToDo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private boolean completed;
    private String username;
    private LocalDate createdDate;

    private LocalDate lastDoneDate;
    private Integer intervalDays;
    private LocalDate nextDueDate;
    private boolean notifyUser;
    @ManyToOne
    @JoinColumn(name = "todo_list_id")
    private ToDoList toDoList;

    @PrePersist
    public void onCreate() {
        if (createdDate == null) createdDate = LocalDate.now();

        if (lastDoneDate == null) lastDoneDate = createdDate;
        if (intervalDays != null && intervalDays > 0) {
            nextDueDate = lastDoneDate.plusDays(intervalDays);
        }
    }

    @PreUpdate
    public void onUpdate() {
        if (intervalDays != null && intervalDays > 0 && lastDoneDate != null) {
            nextDueDate = lastDoneDate.plusDays(intervalDays);
        }
    }
}
