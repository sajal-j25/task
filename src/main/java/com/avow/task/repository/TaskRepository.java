package com.avow.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avow.task.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
	
}
