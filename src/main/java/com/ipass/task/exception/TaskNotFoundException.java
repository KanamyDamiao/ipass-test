package com.ipass.task.exception;

import com.ipass.config.BusinessException;
import org.springframework.http.HttpStatus;

public class TaskNotFoundException extends BusinessException {

	public TaskNotFoundException() {
		super("TaskNotFoundException.message", HttpStatus.BAD_REQUEST.value());
	}
}

