package com.avow.task.service.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.avow.task.dto.CreateTaskDTO;
import com.avow.task.dto.TaskDTO;
import com.avow.task.exception.TaskNotFoundException;
import com.avow.task.mapper.TaskMapper;
import com.avow.task.model.Task;
import com.avow.task.repository.TaskRepository;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void getAllTasks() {
        Task task1 = createNewTask(1L, "Task 1", false);
        Task task2 = createNewTask(2L, "Task 2", true);
        List<Task> tasks = Arrays.asList(task1, task2);

        when(taskRepository.findAll()).thenReturn(tasks);

        TaskDTO taskDto1 = createTaskDTO(1L, "Task 1", false);
        TaskDTO taskDto2 = createTaskDTO(2L, "Task 2", true);
        when(taskMapper.toTaskDTO(task1)).thenReturn(taskDto1);
        when(taskMapper.toTaskDTO(task2)).thenReturn(taskDto2);

        List<TaskDTO> result = taskService.getAllTasks();

        assertEquals(2, result.size());
        assertEquals(taskDto1, result.get(0));
        assertEquals(taskDto2, result.get(1));
    }

    @Test
    void createTask() {
        CreateTaskDTO createTaskDto = createCreateTaskDTO("New Task");
        Task task = createNewTask(null, "New Task", false);

        when(taskMapper.toEntity(createTaskDto)).thenReturn(task);

        Task savedTask = createNewTask(1L, "New Task", false);
        when(taskRepository.save(task)).thenReturn(savedTask);

        TaskDTO savedTaskDto = createTaskDTO(1L, "New Task", false);
        when(taskMapper.toTaskDTO(savedTask)).thenReturn(savedTaskDto);

        TaskDTO result = taskService.createTask(createTaskDto);

        assertEquals(savedTaskDto, result);
    }

    @Test
    void markTaskAsDone() throws TaskNotFoundException {
        Long taskId = 1L;
        Task task = createNewTask(taskId, "Task 1", false);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        Task savedTask = createNewTask(taskId, "Task 1", true);
        when(taskRepository.save(task)).thenReturn(savedTask);

        TaskDTO savedTaskDto = createTaskDTO(taskId, "Task 1", true);
        when(taskMapper.toTaskDTO(savedTask)).thenReturn(savedTaskDto);

        TaskDTO result = taskService.markTaskAsDone(taskId);

        assertEquals(savedTaskDto, result);
    }
    
    @Test
    void markTaskAsDoneThrowsTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.markTaskAsDone(1L))
                .isInstanceOf(TaskNotFoundException.class);

        // Verify that the save method was not called
        verify(taskRepository, never()).save(any());
    }
    
    Task createNewTask(Long id, String title, boolean done){
    	Task task = new Task(id, title, done);
    	return task;    	
    }
    
    TaskDTO createTaskDTO(Long id, String title, boolean done){
    	TaskDTO taskDTO = new TaskDTO(id, title, done);
    	return taskDTO;    	
    }
    
    CreateTaskDTO createCreateTaskDTO(String title){
    	CreateTaskDTO createTaskDTO = new CreateTaskDTO(title);
    	return createTaskDTO;    	
    }
}

