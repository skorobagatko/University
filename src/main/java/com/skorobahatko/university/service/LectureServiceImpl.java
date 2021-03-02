package com.skorobahatko.university.service;

import com.skorobahatko.university.domain.Lecture;
import com.skorobahatko.university.repository.LectureRepository;
import com.skorobahatko.university.service.exception.EntityNotFoundException;
import com.skorobahatko.university.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static com.skorobahatko.university.service.util.Validator.validateId;
import static com.skorobahatko.university.service.util.Validator.validateLecture;

@Service("lectureService")
@Transactional(readOnly = true)
public class LectureServiceImpl implements LectureService {

	private LectureRepository lectureRepository;
	
	@Autowired
	public void setLectureRepository(LectureRepository lectureRepository) {
		this.lectureRepository = lectureRepository;
	}
	
	@Override
	public List<Lecture> getAll() {
		try {
			return lectureRepository.findAll();
		} catch (DataAccessException e) {
			throw new ServiceException("Unable to get lectures list", e);
		}
	}

	@Override
	public Lecture getById(int id) {
		validateId(id);
		
		try {
			return lectureRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			String message = String.format("Lecture with id = %d not found", id);
			throw new EntityNotFoundException(message);
		} catch (DataAccessException e) {
			String message = String.format("Unable to get Lecture with id = %d", id);
			throw new ServiceException(message, e);
		}
	}
	
	@Override
	public List<Lecture> getByCourseId(int courseId) {
		validateId(courseId);
		
		try {
			return lectureRepository.findByCourseId(courseId);
		} catch (DataAccessException e) {
			String message = String.format("Unable to get lectures for course with id = %d", courseId);
			throw new ServiceException(message, e);
		}
	}
	
	@Override
	@Transactional
	public void addAll(List<Lecture> lectures) {
		try {
			lectureRepository.saveAll(lectures);
		} catch (DataAccessException e) {
			String message = String.format("Unable to add lectures: %s", lectures);
			throw new ServiceException(message, e);
		}
	}

	@Override
	@Transactional
	public Lecture add(Lecture lecture) {
		validateLecture(lecture);
		
		try {
			return lectureRepository.save(lecture);
		} catch (DataAccessException e) {
			String message = String.format("Unable to add Lecture: %s", lecture);
			throw new ServiceException(message, e);
		}
	}
	
	@Override
	@Transactional
	public void update(Lecture lecture) {
		validateLecture(lecture);
		
		try {
			lectureRepository.save(lecture);
		} catch (DataAccessException e) {
			String message = String.format("Unable to update Lecture: %s", lecture);
			throw new ServiceException(message, e);
		}
	}

	@Override
	@Transactional
	public void removeById(int id) {
		validateId(id);
		
		try {
			lectureRepository.deleteById(id);
		} catch (DataAccessException e) {
			String message = String.format("Unable to remove Lecture with id = %d", id);
			throw new ServiceException(message, e);
		}
	}
	
	@Override
	@Transactional
	public void removeByCourseId(int courseId) {
		validateId(courseId);
		
		try {
			lectureRepository.deleteByCourseId(courseId);
		} catch (DataAccessException e) {
			String message = String.format("Unable to remove Lecture with course id = %d", courseId);
			throw new ServiceException(message, e);
		}
	}

}
