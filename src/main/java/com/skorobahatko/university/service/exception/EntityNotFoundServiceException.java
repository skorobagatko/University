package com.skorobahatko.university.service.exception;

@SuppressWarnings("serial")
public class EntityNotFoundServiceException extends ServiceException {
	
	public EntityNotFoundServiceException() {
		
	}

	public EntityNotFoundServiceException(String message) {
		super(message);
	}

	public EntityNotFoundServiceException(Throwable cause) {
		super(cause);
	}

	public EntityNotFoundServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
