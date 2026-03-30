package com.ipass.config;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

	private final MessageSource messageSource;

	public GlobalExceptionHandler(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleValidation(MethodArgumentNotValidException ex) {
		Locale locale = LocaleContextHolder.getLocale();

		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", Instant.now());
		body.put("status", HttpStatus.BAD_REQUEST.value());
		body.put("error", "Validation Failed");

		Map<String, String> errors = new HashMap<>();
		for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
			String message = messageSource.getMessage(fieldError, locale);
			errors.put(fieldError.getField(), message);
		}
		body.put("fields", errors);

		return ResponseEntity.badRequest().body(body);
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<Object> handleBusinessException(BusinessException ex) {
		Locale locale = LocaleContextHolder.getLocale();
		String message = messageSource.getMessage(ex.getMessageKey(), null, locale);

		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", Instant.now());
		body.put("status", ex.getStatusCode());
		body.put("error", message);

		return ResponseEntity.status(ex.getStatusCode()).body(body);
	}
}
