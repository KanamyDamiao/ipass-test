package com.ipass.config;

public abstract class BusinessException extends RuntimeException {

	private final String messageKey;
	private final int statusCode;

	protected BusinessException(String messageKey, int statusCode) {
		super(messageKey);
		this.messageKey = messageKey;
		this.statusCode = statusCode;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public int getStatusCode() {
		return statusCode;
	}
}
