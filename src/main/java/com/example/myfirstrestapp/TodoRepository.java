package com.example.myfirstrestapp;

import org.springframework.data.repository.CrudRepository;

import java.util.Set;

// Um mit der Datenbank agieren zu können
public interface TodoRepository extends CrudRepository<Todo,Integer> {

    Set<Todo> findByUserId(int userId);

}
