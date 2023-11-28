package com.avow.task.mapper;

import org.mapstruct.Mapper;

import com.avow.task.dto.CreateTaskDTO;
import com.avow.task.dto.TaskDTO;
import com.avow.task.model.Task;

@Mapper(componentModel = "spring")
public interface TaskMapper {
	
    TaskDTO toTaskDTO(Task task);
    Task toEntity(CreateTaskDTO createTaskDTO);
    
}