package com.todomicroservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "todolist", schema = "todos_service")
public class ToDoList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private LocalDate date;

    @ElementCollection
    @CollectionTable(
            name = "todo_list_members",
            schema = "todos_service",
            joinColumns = @JoinColumn(name = "todo_list_id")
    )
    @Column(name = "member_username")
    private Set<String> members = new HashSet<>();

    @OneToMany(mappedBy = "toDoList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ToDo> toDos = new ArrayList<>();
}

