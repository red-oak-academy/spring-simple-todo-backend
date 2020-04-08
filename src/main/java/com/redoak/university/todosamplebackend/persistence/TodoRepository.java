package com.redoak.university.todosamplebackend.persistence;

import com.redoak.university.todosamplebackend.persistence.entity.TodoEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TodoRepository extends CrudRepository<TodoEntity, Long> {

    Optional<TodoEntity> findFirstByTitle(String title);
}
