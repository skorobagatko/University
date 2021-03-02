package com.skorobahatko.university.controller.rest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.skorobahatko.university.service.exception.EntityNotFoundException;
import com.skorobahatko.university.service.exception.ServiceException;
import com.skorobahatko.university.service.exception.ValidationException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(value = { EntityNotFoundException.class })
	protected ResponseEntity<Object> handleEntityNotFound(RuntimeException ex, WebRequest request) {
		String bodyOfResponse = "Entity not found";
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	
	@ExceptionHandler(value = { ValidationException.class })
	protected ResponseEntity<Object> handleValidationException(RuntimeException ex, WebRequest request) {
		String bodyOfResponse = "Validation error!";
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
	@ExceptionHandler(value = { ServiceException.class })
	protected ResponseEntity<Object> handleServiceException(RuntimeException ex, WebRequest request) {
		String bodyOfResponse = "Something went wrong on the server!";
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	}

}
