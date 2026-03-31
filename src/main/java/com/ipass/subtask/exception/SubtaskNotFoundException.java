package com.ipass.subtask.exception;

import com.ipass.config.BusinessException;
import org.springframework.http.HttpStatus;

public class SubtaskNotFoundException extends BusinessException {

	public SubtaskNotFoundException() {
		super("SubtaskNotFoundException.message", HttpStatus.BAD_REQUEST.value());
	}
}

