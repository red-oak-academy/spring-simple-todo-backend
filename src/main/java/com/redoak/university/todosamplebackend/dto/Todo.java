package com.redoak.university.todosamplebackend.dto;


import com.redoak.university.todosamplebackend.persistence.entity.TodoEntity;
import lombok.Data;

@Data
public class Todo {

    private long id;
    private String title;
    private String description;
    private TodoStatus status;

    public Todo() {}

    public Todo(TodoEntity originEntity) {
        this.setId(originEntity.getId());
        this.setStatus(originEntity.getStatus());
        this.setDescription(originEntity.getDescription());
        this.setTitle(originEntity.getTitle());
    }
}
