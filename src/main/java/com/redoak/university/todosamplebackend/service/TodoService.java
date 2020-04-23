package com.redoak.university.todosamplebackend.service;

import com.redoak.university.todosamplebackend.dto.Todo;
import com.redoak.university.todosamplebackend.dto.TodoStatus;
import com.redoak.university.todosamplebackend.persistence.TodoRepository;
import com.redoak.university.todosamplebackend.persistence.entity.TodoEntity;
import com.redoak.university.todosamplebackend.service.exception.TodoCreationException;
import com.redoak.university.todosamplebackend.service.exception.TodoDeleteException;
import com.redoak.university.todosamplebackend.service.exception.TodoRequestException;
import com.redoak.university.todosamplebackend.service.exception.TodoUpdateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    public Todo createTodo(Todo todo) throws TodoCreationException {

        if (todo.getStatus() != null && todo.getStatus() != TodoStatus.TODO) {
            throw new TodoCreationException("Todo Status must be TODO or empty.");
        } else {
            todo.setStatus(TodoStatus.TODO);
        }

        if (todoRepository.findFirstByTitle(todo.getTitle()).isPresent()) {
            throw new TodoCreationException("Todo with title " + todo.getTitle() + "already exists.");
        }

        final TodoEntity savedEntity = todoRepository.save(new TodoEntity(todo));
        return new Todo(savedEntity);
    }

    public Todo updateTodo(Todo todo, long id) throws TodoUpdateException {

        final TodoEntity todoEntity = todoRepository.findById(id)
                .orElseThrow(() -> new TodoUpdateException("Could not find corresponding Todo!"));

        if (todoRepository.findFirstByTitle(todo.getTitle()).isPresent()) {
            throw new TodoUpdateException("Todo with title " + todo.getTitle() + " already exists.");
        }

        if (todo.getStatus() == TodoStatus.DONE && todoEntity.getStatus() == TodoStatus.TODO) {
            throw new TodoUpdateException("Cannot transition from TODO into DONE!");
        } else if (todo.getStatus() != null) {
            todoEntity.setStatus(todo.getStatus());
        }

        if (todo.getTitle() != null) {
            todoEntity.setTitle(todo.getTitle());
        }

        if (todo.getDescription() != null) {
            todoEntity.setDescription(todo.getDescription());
        }

        TodoEntity updatedEntity = todoRepository.save(todoEntity);
        return new Todo(updatedEntity);
    }

    public void deleteTodo(long id) throws TodoDeleteException {

        final Optional<TodoEntity> byId = todoRepository.findById(id);

        if (byId.isPresent()) {
            todoRepository.delete(byId.get());
        } else {
            throw new TodoDeleteException("Could not find Todo with ID " + id);
        }
    }

    public Todo getTodo(long id) throws TodoRequestException {

        final TodoEntity todoEntity = todoRepository.findById(id).orElseThrow(() -> new TodoRequestException("Could not find corresponding Todo!"));
        return new Todo(todoEntity);
    }
}
