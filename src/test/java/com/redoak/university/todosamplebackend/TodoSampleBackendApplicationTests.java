package com.redoak.university.todosamplebackend;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.redoak.university.todosamplebackend.dto.Todo;
import com.redoak.university.todosamplebackend.dto.TodoStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestExecutionListeners(value = {DbUnitTestExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
class TodoSampleBackendApplicationTests {


    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @DatabaseSetup("/testGetTodoSetup.xml")
    public void testGetTodo() {

        ResponseEntity<Todo> forEntity = this.testRestTemplate.getForEntity("/todos/1", Todo.class);
        Todo body = forEntity.getBody();

        assertEquals(HttpStatus.OK, forEntity.getStatusCode(), "Wrong HTTP Status Code");
        assertEquals( "Title", body.getTitle(), "Wrong Title");
        assertEquals("Description", body.getDescription(), "Wrong Description");
        assertEquals(TodoStatus.TODO, body.getStatus(), "Wrong Status");
        assertEquals(1L, body.getId(), "Wrong ID");
    }

    @Test
    @DatabaseSetup("/testUpdateTodoSetup.xml")
    @ExpectedDatabase("/testUpdateTodoExpected.xml")
    public void testUpdateTodo() {

        Todo requestTodo = new Todo();
        requestTodo.setTitle("New Title");

        HttpEntity<Todo> requestEntity = new HttpEntity<>(requestTodo);

        ResponseEntity<Todo> responseBody = this.testRestTemplate.exchange("/todos/1", HttpMethod.PUT, requestEntity, Todo.class);

        //check if the updated values are returned in the response.
        Todo response = responseBody.getBody();
        assertEquals("New Title", response.getTitle(), "Wrong Title");
        assertEquals("Description", response.getDescription(), "Wrong Description");
        assertEquals(TodoStatus.TODO, response.getStatus(), "Wrong Status");
        assertEquals(1L, response.getId(), "Wrong ID");
    }

    @Test
    @DatabaseSetup("/testDeleteTodoSetup.xml")
    @ExpectedDatabase("/testDeleteTodoExpected.xml")
    public void testDeleteTodo() {
        this.testRestTemplate.delete("/todos/1");
    }

    @Test
    @DatabaseSetup("/testCreateTodoSetup.xml")
    @ExpectedDatabase("/testCreateTodoExpected.xml")
    public void testCreateTodo() {
        Todo todoToCreate = new Todo();
        todoToCreate.setTitle("Title");
        todoToCreate.setDescription("Description");
        todoToCreate.setStatus(TodoStatus.TODO);

        this.testRestTemplate.postForEntity("/todos", todoToCreate, Todo.class);
    }

}
