package com.mrahmed.todo.restcontroller;

import com.mrahmed.todo.entity.Todo;
import com.mrahmed.todo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Project: todo
 * @Author: M. R. Ahmed
 * @Created at: 12/12/2024
 */
@RestController
@RequestMapping("/api/todos")
public class TodoController {

    @Autowired
    private TodoRepository todoRepository;

    @PostMapping("/")
    public ResponseEntity<Todo> createTodo(@RequestBody Todo todo) {
        try {
            Todo savedTodo = todoRepository.save(todo);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedTodo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/")
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

}
