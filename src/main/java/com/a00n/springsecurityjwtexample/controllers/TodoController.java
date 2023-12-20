package com.a00n.springsecurityjwtexample.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.a00n.springsecurityjwtexample.entities.Todo;
import com.a00n.springsecurityjwtexample.entities.User;
import com.a00n.springsecurityjwtexample.repositories.TodoRepository;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/todos")
@CrossOrigin("*")
public class TodoController {

    @Autowired
    private TodoRepository todoRepository;

    @GetMapping
    public List<Todo> findAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            User user = (User) authentication.getPrincipal();
            return todoRepository.findByUser(user);
        }
        return null;
    }

    @PostMapping
    public Todo create(@RequestBody Todo todo, Authentication authentication) {
        todo.setUser((User) authentication.getPrincipal());
        return todoRepository.save(todo);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        if (todoRepository.existsById(id)) {
            todoRepository.deleteById(id);
            return ResponseEntity.ok("Todo Deleted");
        } else {
            return ResponseEntity.badRequest().body("Todo not found");
        }
    }

}
