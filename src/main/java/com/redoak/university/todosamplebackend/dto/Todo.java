package com.redoak.university.todosamplebackend.dto;


import com.redoak.university.todosamplebackend.persistence.entity.TodoEntity;

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


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TodoStatus getStatus() {
        return status;
    }

    public void setStatus(TodoStatus status) {
        this.status = status;
    }
}
