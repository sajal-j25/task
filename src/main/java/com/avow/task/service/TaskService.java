package com.avow.task.service;

import java.util.List;

import com.avow.task.dto.CreateTaskDTO;
import com.avow.task.dto.TaskDTO;
import com.avow.task.exception.TaskNotFoundException;

public interface TaskService {
	
    List<TaskDTO> getAllTasks();
    TaskDTO createTask(CreateTaskDTO createTaskDTO);
    TaskDTO markTaskAsDone(Long id) throws TaskNotFoundException;
    
}
