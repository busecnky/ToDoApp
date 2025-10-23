package com.todomicroservice.repository;

import com.todomicroservice.entity.ToDoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToDoListRepository extends JpaRepository<ToDoList, Long> {
    @Query("SELECT t.title FROM ToDoList t WHERE :username MEMBER OF t.members")
    List<String> findTitlesByMember(@Param("username") String username);}
