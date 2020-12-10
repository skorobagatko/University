package com.skorobahatko.university.dao.exception;

@SuppressWarnings("serial")
public class EntityNotFoundDaoException extends DaoException {

	public EntityNotFoundDaoException() {
	}

	public EntityNotFoundDaoException(String message) {
		super(message);
	}

	public EntityNotFoundDaoException(Throwable cause) {
		super(cause);
	}

	public EntityNotFoundDaoException(String message, Throwable cause) {
		super(message, cause);
	}

}