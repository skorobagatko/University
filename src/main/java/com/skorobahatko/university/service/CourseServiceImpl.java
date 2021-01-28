package com.skorobahatko.university.service;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.skorobahatko.university.dao.CourseDao;
import com.skorobahatko.university.dao.exception.DaoException;
import com.skorobahatko.university.dao.exception.EntityNotFoundDaoException;
import com.skorobahatko.university.domain.Course;
import com.skorobahatko.university.service.exception.EntityNotFoundServiceException;
import com.skorobahatko.university.service.exception.ServiceException;
import com.skorobahatko.university.service.exception.ValidationException;

public class CourseServiceImpl implements CourseService {
	
	private CourseDao courseDao;
	
	public void setCourseDao(CourseDao courseDao) {
		this.courseDao = courseDao;
	}

	@Override
	public List<Course> getAll() {
		try {
			return courseDao.getAll();
		} catch (DaoException e) {
			throw new ServiceException("Unable to get courses list", e);
		}
	}
	
	@Override
	public Course getById(int id) {
		validateId(id);
		
		try {
			return courseDao.getById(id);
		} catch (EntityNotFoundDaoException e) {
			String message = String.format("Course with id = %d not found", id);
			throw new EntityNotFoundServiceException(message);
		} catch (DaoException e) {
			String message = String.format("Unable get Course with id = %d", id);
			throw new ServiceException(message, e);
		}
	}

	@Override
	public void add(Course course) {
		validateCourse(course);
		
		try {
			courseDao.add(course);
		} catch (DaoException e) {
			String message = String.format("Unable to add Course: %s", course);
			throw new ServiceException(message, e);
		}
	}
	
	@Override
	public void update(Course course) {
		validateCourse(course);
		
		try {
			courseDao.update(course);
		} catch (DaoException e) {
			String message = String.format("Unable to update Course: %s", course);
			throw new ServiceException(message, e);
		}		
	}

	@Override
	public void removeById(int id) {
		validateId(id);
		
		try {
			courseDao.removeById(id);
		} catch (DataAccessException e) {
			String message = String.format("Unable to remove Course with id = %d", id);
			throw new ServiceException(message, e);
		}
	}
	
	private void validateId(int id) {
		if (id <= 0) {
			String message = String.format("ID must be a positive integer value. Actually, ID was %d", id);
			throw new ValidationException(message);
		}
	}
	
	private void validateCourse(Course course) {
		if (course == null) {
			String message = String.format("Course must not be null. Actually, Course was %s", course);
			throw new ValidationException(message);
		}
	}

}
