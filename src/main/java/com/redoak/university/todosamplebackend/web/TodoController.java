package com.redoak.university.todosamplebackend.web;

import com.redoak.university.todosamplebackend.dto.Todo;
import com.redoak.university.todosamplebackend.service.TodoService;
import com.redoak.university.todosamplebackend.service.exception.TodoCreationException;
import com.redoak.university.todosamplebackend.service.exception.TodoDeleteException;
import com.redoak.university.todosamplebackend.service.exception.TodoRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping("/todos/{id}")
    public Todo getTodo(@PathVariable long id) throws TodoRequestException {
        return todoService.getTodo(id);
    }

    @PostMapping("/todos")
    public Todo createTodo(@RequestBody Todo todo) throws TodoCreationException {
        return todoService.createTodo(todo);
    }

    @PutMapping("/todos/{id}")
    public Todo updateTodo(@RequestBody Todo todo, @PathVariable long id) throws Exception {
        return todoService.updateTodo(todo, id);
    }

    @DeleteMapping("/todos/{id}")
    public void deleteTodo(@PathVariable long id) throws TodoDeleteException {
        todoService.deleteTodo(id);
    }
}
