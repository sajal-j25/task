package com.avow.task.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.avow.task.dto.CreateTaskDTO;
import com.avow.task.dto.TaskDTO;
import com.avow.task.service.TaskService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Test
    void getAllTasks() throws Exception {
        TaskDTO taskDto1 = new TaskDTO(1L, "Task 1", false);
        TaskDTO taskDto2 = new TaskDTO(2L, "Task 2", true);

        when(taskService.getAllTasks()).thenReturn(Arrays.asList(taskDto1, taskDto2));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[0].done").value(false))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].title").value("Task 2"))
                .andExpect(jsonPath("$[1].done").value(true));
    }

    @Test
    void createTask() throws Exception {
        CreateTaskDTO createTaskDto = new CreateTaskDTO("New Task");
        TaskDTO savedTaskDto = new TaskDTO(1L, "New Task", false);

        when(taskService.createTask(createTaskDto)).thenReturn(savedTaskDto);

        mockMvc.perform(post("/tasks")
                .contentType("application/json")
                .content("{\"title\":\"New Task\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("New Task"))
                .andExpect(jsonPath("$.done").value(false));
    }

    @Test
    void markTaskAsDone() throws Exception {
        // Arrange
        Long taskId = 1L;
        TaskDTO savedTaskDto = new TaskDTO(taskId, "Task 1", true);

        when(taskService.markTaskAsDone(taskId)).thenReturn(savedTaskDto);
        
        mockMvc.perform(put("/tasks/1/done"))
		        .andExpect(status().isOk())
		        .andExpect(jsonPath("$.done").value(true));
    }
}