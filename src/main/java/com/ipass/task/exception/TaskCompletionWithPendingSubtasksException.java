package com.ipass.task.exception;

import com.ipass.config.BusinessException;
import org.springframework.http.HttpStatus;

public class TaskCompletionWithPendingSubtasksException extends BusinessException {

	public TaskCompletionWithPendingSubtasksException() {
		super("TaskCompletionWithPendingSubtasksException.message", HttpStatus.BAD_REQUEST.value());
	}
}

