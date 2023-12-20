package com.a00n.springsecurityjwtexample.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.a00n.springsecurityjwtexample.entities.Todo;
import com.a00n.springsecurityjwtexample.entities.User;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByUser(User user);
}
