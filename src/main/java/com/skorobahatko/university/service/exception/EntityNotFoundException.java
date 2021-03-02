package com.skorobahatko.university.service.exception;

@SuppressWarnings("serial")
public class EntityNotFoundException extends ServiceException {
	
	public EntityNotFoundException() {
		
	}

	public EntityNotFoundException(String message) {
		super(message);
	}

	public EntityNotFoundException(Throwable cause) {
		super(cause);
	}

	public EntityNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
