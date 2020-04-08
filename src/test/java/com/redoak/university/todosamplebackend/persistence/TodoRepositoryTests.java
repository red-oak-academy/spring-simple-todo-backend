package com.redoak.university.todosamplebackend.persistence;

import com.redoak.university.todosamplebackend.dto.TodoStatus;
import com.redoak.university.todosamplebackend.persistence.entity.TodoEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Isolated Tests for the Persistence Layer.
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class TodoRepositoryTests {


    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TodoRepository todoRepository;


    @Test
    public void testFindFirstByTitle() {

        TodoEntity existingTodo = new TodoEntity();
        existingTodo.setTitle("Existing Title");
        existingTodo.setDescription("Description");
        existingTodo.setStatus(TodoStatus.TODO);

        entityManager.persist(existingTodo);

        Optional<TodoEntity> existingTitle = todoRepository.findFirstByTitle("Existing Title");
        assertTrue(existingTitle.isPresent());

        TodoEntity todoEntity = existingTitle.get();

        assertEquals("Description", todoEntity.getDescription(), "Descriptions did not match");
        assertEquals( "Existing Title", todoEntity.getTitle(), "Titles did not match");
        assertEquals(TodoStatus.TODO, todoEntity.getStatus(), "Status did not match");

        Optional<TodoEntity> nonExistingTitle = todoRepository.findFirstByTitle("Non Existing Title");
        assertFalse(nonExistingTitle.isPresent());
    }
}
