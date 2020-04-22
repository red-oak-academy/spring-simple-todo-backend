package com.redoak.university.todosamplebackend.service;

import com.redoak.university.todosamplebackend.dto.Todo;
import com.redoak.university.todosamplebackend.dto.TodoStatus;
import com.redoak.university.todosamplebackend.persistence.TodoRepository;
import com.redoak.university.todosamplebackend.persistence.entity.TodoEntity;
import com.redoak.university.todosamplebackend.service.exception.TodoCreationException;
import com.redoak.university.todosamplebackend.service.exception.TodoDeleteException;
import com.redoak.university.todosamplebackend.service.exception.TodoRequestException;
import com.redoak.university.todosamplebackend.service.exception.TodoUpdateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Unit Tests for TodoService.
 */
@ExtendWith(SpringExtension.class)
public class TodoServiceTests {


    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @Test
    public void createTodoShouldSetStatusIfNoneIsPassed() throws TodoCreationException {

        //Given
        Todo todo = new Todo();
        todo.setTitle("TODO Title");
        todo.setDescription("TODO Description");

        //Tell the Repository mock to just return the first argument passed to it.
        when(todoRepository.save(any())).then(returnsFirstArg());

        //When
        final Todo returnedTodo = todoService.createTodo(todo);

        //Then
        assertEquals(TodoStatus.TODO, returnedTodo.getStatus(), "Todo Status was not TODO");
    }

    @Test
    public void createTodoShouldThrowExceptionWhenWrongStatusIsPassed() {

        //Given
        Todo todo = new Todo();
        todo.setTitle("TODO Title");
        todo.setDescription("TODO Description");
        todo.setStatus(TodoStatus.IN_PROGRESS);

        //When, Then
        Assertions.assertThrows(TodoCreationException.class, () -> todoService.createTodo(todo));
    }

    @Test
    public void createTodoShouldThrowExceptionIfTitleIsDuplicated() {

        //Given
        Todo todo = new Todo();
        todo.setTitle("Duplicated Title");
        todo.setDescription("Description");

        //Returning a filled optional when searching for the title
        when(todoRepository.findFirstByTitle("Duplicated Title")).thenReturn(Optional.of(new TodoEntity()));

        //When
        Assertions.assertThrows(TodoCreationException.class, () -> todoService.createTodo(todo));
    }

    @Test
    public void updateTodoShouldThrowExeptionIfTodoIsNotFound() {

        //Given
        Todo todo = new Todo();
        todo.setTitle("Title");
        todo.setDescription("Description");

        when(todoRepository.findById(1L)).thenReturn(Optional.empty());

        //When
        Assertions.assertThrows(TodoUpdateException.class, () -> todoService.updateTodo(todo, 1L));
    }

    @Test
    public void updateTodoShouldThrowExceptionIfNewTitleIsDuplicated() {

        //Given
        TodoEntity existingTodo = new TodoEntity();
        existingTodo.setTitle("Title");
        existingTodo.setDescription("Description");
        existingTodo.setStatus(TodoStatus.TODO);

        TodoEntity todoWithDuplicatedTitle = new TodoEntity();
        todoWithDuplicatedTitle.setTitle("Duplicated Title");

        Todo todo = new Todo();
        todo.setTitle("Duplicated Title");
        todo.setDescription("Description");

        when(todoRepository.findById(1L)).thenReturn(Optional.of(existingTodo));
        when(todoRepository.findFirstByTitle("Duplicated Title")).thenReturn(Optional.of(todoWithDuplicatedTitle));

        //When, Then
        Assertions.assertThrows(TodoUpdateException.class, () -> todoService.updateTodo(todo, 1L));
    }

    @Test
    public void updateTodoCannotTransitionFromTodoToDone() {

        //Given
        TodoEntity existingTodo = new TodoEntity();
        existingTodo.setTitle("Title");
        existingTodo.setDescription("Description");
        existingTodo.setStatus(TodoStatus.TODO);

        Todo updatedTodo = new Todo();
        updatedTodo.setStatus(TodoStatus.DONE);

        when(todoRepository.findById(1L)).thenReturn(Optional.of(existingTodo));

        //When
        Assertions.assertThrows(TodoUpdateException.class, () -> todoService.updateTodo(updatedTodo, 1L));
    }


    @Test
    public void updateTodoShouldUpdateAllFields() throws TodoUpdateException {

        //Given
        Todo updateTodo = new Todo();
        updateTodo.setTitle("New Title");
        updateTodo.setDescription("New Description");
        updateTodo.setStatus(TodoStatus.IN_PROGRESS);

        TodoEntity existingTodoEntity = new TodoEntity();
        existingTodoEntity.setId(1L);
        existingTodoEntity.setTitle("Old Title");
        existingTodoEntity.setDescription("Old Description");
        existingTodoEntity.setStatus(TodoStatus.TODO);

        when(todoRepository.findById(1L)).thenReturn(Optional.of(existingTodoEntity));
        when(todoRepository.save(any())).thenAnswer(returnsFirstArg());

        //When
        Todo updatedTodo = todoService.updateTodo(updateTodo, 1L);

        //Then
        ArgumentCaptor<TodoEntity> entityCaptor = ArgumentCaptor.forClass(TodoEntity.class);
        verify(todoRepository).save(entityCaptor.capture());

        //Check values of entity that was passed to repository
        TodoEntity savedEntity = entityCaptor.getValue();
        assertEquals("New Title", savedEntity.getTitle(), "Title did not match in Entity");
        assertEquals("New Description", savedEntity.getDescription(), "Description did not match in Entity");
        assertEquals(TodoStatus.IN_PROGRESS, savedEntity.getStatus(), "Status did not match in Entity");

        //Check values of the returned To-Do
        assertEquals("New Title", updatedTodo.getTitle(), "Title did not match in returned Todo");
        assertEquals("New Description", updatedTodo.getDescription(), "Description did not match in returned Todo");
        assertEquals(TodoStatus.IN_PROGRESS, updatedTodo.getStatus(), "Status did not match in returned Todo");
    }








    @Test
    public void deleteTodoShouldCallDelete() throws TodoDeleteException {

        //Given
        when(todoRepository.findById(1L)).thenReturn(Optional.of(new TodoEntity()));

        //When
        todoService.deleteTodo(1L);

        //Then
        verify(todoRepository, times(1)).delete(any());
    }


    @Test
    public void deleteTodoShouldThrowExceptionIfNoTodoIsFound() {

        //Given
        when(todoRepository.findById(1L)).thenReturn(Optional.empty());

        //When
        Assertions.assertThrows(TodoDeleteException.class, () -> todoService.deleteTodo(1L));
    }

    @Test
    public void getTodoShouldReturnTodoWithCorrectValues() throws TodoRequestException {

        //Given
        TodoEntity todoEntity = new TodoEntity();
        todoEntity.setTitle("Title");
        todoEntity.setDescription("Description");
        todoEntity.setStatus(TodoStatus.TODO);
        todoEntity.setId(1L);

        when(todoRepository.findById(1L)).thenReturn(Optional.of(todoEntity));

        //When
        final Todo returnedTodo = todoService.getTodo(1L);

        //Then
        assertEquals("Title", returnedTodo.getTitle(), "Title did not match");
        assertEquals("Description", returnedTodo.getDescription(), "Description did not match");
        assertEquals(TodoStatus.TODO, returnedTodo.getStatus(), "Status did not match");
    }

    @Test
    public void getTodoShouldThrowExceptionIfNoTodoIsFound() {

        //Given
        when(todoRepository.findById(1L)).thenReturn(Optional.empty());

        //When
        Assertions.assertThrows(TodoRequestException.class, () -> todoService.getTodo(1L));
    }
}

