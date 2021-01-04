package com.skorobahatko.university.service;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.skorobahatko.university.dao.LectureDao;
import com.skorobahatko.university.dao.exception.DaoException;
import com.skorobahatko.university.dao.exception.EntityNotFoundDaoException;
import com.skorobahatko.university.domain.Lecture;
import com.skorobahatko.university.service.exception.EntityNotFoundServiceException;
import com.skorobahatko.university.service.exception.ServiceException;
import com.skorobahatko.university.service.exception.ValidationException;

public class LectureServiceImpl implements LectureService {

	private LectureDao lectureDao;
	
	public void setLectureDao(LectureDao lectureDao) {
		this.lectureDao = lectureDao;
	}
	
	@Override
	public List<Lecture> getAll() {
		try {
			return lectureDao.getAll();
		} catch (DaoException e) {
			throw new ServiceException("Unable to get lectures list", e);
		}
	}

	@Override
	public Lecture getById(int id) {
		validateId(id);
		
		try {
			return lectureDao.getById(id);
		} catch (EntityNotFoundDaoException e) {
			String message = String.format("Lecture with id = %d not found", id);
			throw new EntityNotFoundServiceException(message);
		} catch (DaoException e) {
			String message = String.format("Unable to get Lecture with id = %d", id);
			throw new ServiceException(message, e);
		}
	}
	
	@Override
	public List<Lecture> getByCourseId(int courseId) {
		validateId(courseId);
		
		try {
			return lectureDao.getByCourseId(courseId);
		} catch (DaoException e) {
			String message = String.format("Unable to get lectures for course with id = %d", courseId);
			throw new ServiceException(message, e);
		}
	}
	
	@Override
	public void addAll(List<Lecture> lectures) {
		try {
			lectureDao.addAll(lectures);
		} catch (DaoException e) {
			String message = String.format("Unable to add lectures: %s", lectures);
			throw new ServiceException(message, e);
		}
	}

	@Override
	public void add(Lecture lecture) {
		validateLecture(lecture);
		
		try {
			lectureDao.add(lecture);
		} catch (DaoException e) {
			String message = String.format("Unable to add Lecture: %s", lecture);
			throw new ServiceException(message, e);
		}
	}
	
	@Override
	public void update(Lecture lecture) {
		validateLecture(lecture);
		
		try {
			lectureDao.update(lecture);
		} catch (DaoException e) {
			String message = String.format("Unable to update Lecture: %s", lecture);
			throw new ServiceException(message, e);
		}
	}

	@Override
	public void removeById(int id) {
		validateId(id);
		
		try {
			lectureDao.removeById(id);
		} catch (DataAccessException e) {
			String message = String.format("Unable to remove Lecture with id = %d", id);
			throw new ServiceException(message, e);
		}
	}
	
	@Override
	public void removeByCourseId(int courseId) {
		validateId(courseId);
		
		try {
			lectureDao.removeByCourseId(courseId);
		} catch (DataAccessException e) {
			String message = String.format("Unable to remove Lecture with course id = %d", courseId);
			throw new ServiceException(message, e);
		}
	}
	
	private void validateId(int id) {
		if (id <= 0) {
			String message = String.format("ID must be a positive integer value. Actually, ID was %d", id);
			throw new ValidationException(message);
		}
	}
	
	private void validateLecture(Lecture lecture) {
		if (lecture == null) {
			String message = String.format("Lecture must not be null. Actually, Lecture was %s", lecture);
			throw new ValidationException(message);
		}
	}

}
