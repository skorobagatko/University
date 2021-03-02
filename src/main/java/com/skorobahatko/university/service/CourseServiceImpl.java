package com.skorobahatko.university.service;

import com.skorobahatko.university.domain.Course;
import com.skorobahatko.university.repository.CourseRepository;
import com.skorobahatko.university.service.exception.EntityNotFoundException;
import com.skorobahatko.university.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static com.skorobahatko.university.service.util.Validator.validateCourse;
import static com.skorobahatko.university.service.util.Validator.validateId;

@Service("courseService")
@Transactional(readOnly = true)
public class CourseServiceImpl implements CourseService {
	
	private CourseRepository courseRepository;
	
	@Autowired
	public void setCourseRepository(CourseRepository courseRepository) {
		this.courseRepository = courseRepository;
	}

	@Override
	public List<Course> getAll() {
		try {
			return courseRepository.findAll();
		} catch (DataAccessException e) {
			throw new ServiceException("Unable to get courses list", e);
		}
	}
	
	@Override
	public Course getById(int id) {
		validateId(id);
		
		try {
			return courseRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			String message = String.format("Course with id = %d not found", id);
			throw new EntityNotFoundException(message);
		} catch (DataAccessException e) {
			String message = String.format("Unable get Course with id = %d", id);
			throw new ServiceException(message, e);
		}
	}

	@Override
	@Transactional
	public Course add(Course course) {
		validateCourse(course);
		
		try {
			return courseRepository.save(course);
		} catch (DataAccessException e) {
			String message = String.format("Unable to add Course: %s", course);
			throw new ServiceException(message, e);
		}
	}
	
	@Override
	@Transactional
	public void update(Course course) {
		validateCourse(course);
		
		try {
			courseRepository.save(course);
		} catch (DataAccessException e) {
			String message = String.format("Unable to update Course: %s", course);
			throw new ServiceException(message, e);
		}		
	}

	@Override
	@Transactional
	public void removeById(int id) {
		validateId(id);
		
		try {
			courseRepository.deleteById(id);
		} catch (DataAccessException e) {
			String message = String.format("Unable to remove Course with id = %d", id);
			throw new ServiceException(message, e);
		}
	}

}
