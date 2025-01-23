package com.todomicroservice.repository;

import com.todomicroservice.entity.ToDo;

public interface ToDoRepository extends JpaRepository<ToDo, Integer> {
}
