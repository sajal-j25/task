package com.avow.task.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.avow.task.dto.*;
import com.avow.task.exception.TaskNotFoundException;
import com.avow.task.mapper.TaskMapper;
import com.avow.task.model.Task;
import com.avow.task.repository.TaskRepository;
import com.avow.task.service.TaskService;

@Service
public class TaskServiceImpl implements TaskService {
	
	 private final TaskRepository taskRepository;
	 private TaskMapper taskMapper;
	
	 @Autowired
	 public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper) {
	     this.taskRepository = taskRepository;
	     this.taskMapper = taskMapper;
	 }
	
	 @Override
	 public List<TaskDTO> getAllTasks() {
	     return taskRepository.findAll().stream()
	             .map(taskMapper::toTaskDTO)
	             .collect(Collectors.toList());
	 }
	
	 @Override
	 public TaskDTO createTask(CreateTaskDTO createTaskDTO) {
	     Task task = taskMapper.toEntity(createTaskDTO);
	     Task savedTask = taskRepository.save(task);
	     return taskMapper.toTaskDTO(savedTask);
	 }
	
	 @Override
	 public TaskDTO markTaskAsDone(Long id) throws TaskNotFoundException {
	     Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException());
	     task.setDone(true);
	     Task savedTask = taskRepository.save(task);
	     return taskMapper.toTaskDTO(savedTask);
	 }
}
