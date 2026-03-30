package com.ipass.user.exception;

import com.ipass.config.BusinessException;
import org.springframework.http.HttpStatus;

public class UserEmailAlreadyInUseException extends BusinessException {

	public UserEmailAlreadyInUseException() {
		super("UserEmailAlreadyInUseException.message", HttpStatus.BAD_REQUEST.value());
	}
}
