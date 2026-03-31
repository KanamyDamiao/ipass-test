package com.ipass.task.repository;

import com.ipass.task.domain.TaskModel;
import com.ipass.task.domain.TaskStatus;
import com.ipass.task.exception.TaskNotFoundException;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<TaskModel, UUID> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	default TaskModel findByIdOrThrowNotFound(UUID id) {
		return findById(id).orElseThrow(TaskNotFoundException::new);
	}

	List<TaskModel> findByStatus(TaskStatus status);
}

