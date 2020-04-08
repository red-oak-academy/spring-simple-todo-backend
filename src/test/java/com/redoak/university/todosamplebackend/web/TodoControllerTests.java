package com.redoak.university.todosamplebackend.web;


import com.redoak.university.todosamplebackend.dto.Todo;
import com.redoak.university.todosamplebackend.dto.TodoStatus;
import com.redoak.university.todosamplebackend.service.TodoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.*;
import java.util.Scanner;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Isolated Tests for the Web Layer.
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(TodoController.class)
public class TodoControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TodoService todoService;

    @Test
    public void testGetTodoShouldReturnCorrectJson() throws Exception {

        Todo todo = new Todo();
        todo.setTitle("Title");
        todo.setDescription("Description");
        todo.setStatus(TodoStatus.TODO);
        todo.setId(1L);

        when(todoService.getTodo(1L)).thenReturn(todo);

        this.mvc.perform(get("/todos/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("Description")))
                .andExpect(jsonPath("$.status", is("TODO")))
                .andExpect(jsonPath("$.title", is("Title")));
    }

    @Test
    public void testCreateTodoShouldRespectAllFields() throws Exception {

        String requestBodyString = getContentFromFilename("testCreateTodoShouldRespectAllFields.json");

        //Set an ID an return the requested object.
        when(todoService.createTodo(any())).thenAnswer(invocation -> {
            Todo argument = invocation.getArgument(0);
            argument.setId(1L);
            return argument;
        });

        this.mvc.perform(post("/todos")
                .content(requestBodyString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Title")))
                .andExpect(jsonPath("$.description", is("Description")))
                .andExpect(jsonPath("$.status", is("TODO")))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void testUpdateTodoShouldDisregardNonExistentFields() throws Exception {

        String requestBodyString = "{\"title\": \"Title\"}";

        when(todoService.updateTodo(any(), eq(1L))).thenAnswer(returnsFirstArg());

        this.mvc.perform(put("/todos/1")
                .content(requestBodyString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        ArgumentCaptor<Todo> argCaptor = ArgumentCaptor.forClass(Todo.class);

        verify(todoService, times(1)).updateTodo(argCaptor.capture(), eq(1L));

        Todo value = argCaptor.getValue();
        Assertions.assertNull(value.getDescription(), "Description was not null even if not passed.");
        assertNull(value.getStatus(), "Status was not null even if not passed.");
        assertEquals("Title", value.getTitle(), "Title was not the expected");
    }


    /**
     * Returnes the Contents of a file as string.
     *
     * @param filePath the Path of the file.
     * @return the contents as a string. assumes utf-8 encoding.
     */
    private String getContentFromFilename(String filePath) {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(filePath);
        return new Scanner(resourceAsStream, "utf-8").useDelimiter("\\Z").next();
    }
}
