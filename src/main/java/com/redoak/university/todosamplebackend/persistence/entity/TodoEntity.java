package com.redoak.university.todosamplebackend.persistence.entity;

import com.redoak.university.todosamplebackend.dto.Todo;
import com.redoak.university.todosamplebackend.dto.TodoStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "TODO")
@Getter
@Setter
public class TodoEntity {

    public TodoEntity() {}

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    @SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq")
    private long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "STATUS")
    private TodoStatus status;

    public TodoEntity(Todo todo) {
        this.setTitle(todo.getTitle());
        this.setStatus(todo.getStatus());
        this.setDescription(todo.getDescription());
    }
}
