package com.ipass.user.exception;

import com.ipass.config.BusinessException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BusinessException {

	public UserNotFoundException() {
		super("UserNotFoundException.message", HttpStatus.BAD_REQUEST.value());
	}
}
