package com.todomicroservice.repository;

import com.todomicroservice.entity.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ToDoRepository extends JpaRepository<ToDo, Long> {
    List<ToDo> findByUsername(String username);
    Optional<ToDo> findByIdAndUsername(Long id, String username);
    List<ToDo> findAllByToDoList_Id(Long toDoListId);
}
