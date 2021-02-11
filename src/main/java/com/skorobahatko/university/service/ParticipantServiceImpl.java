package com.skorobahatko.university.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skorobahatko.university.dao.ParticipantDao;
import com.skorobahatko.university.dao.exception.DaoException;
import com.skorobahatko.university.dao.exception.EntityNotFoundDaoException;
import com.skorobahatko.university.domain.Participant;
import com.skorobahatko.university.domain.Student;
import com.skorobahatko.university.domain.Teacher;
import com.skorobahatko.university.service.exception.EntityNotFoundServiceException;
import com.skorobahatko.university.service.exception.ServiceException;
import com.skorobahatko.university.service.exception.ValidationException;

@Service("participantService")
@Transactional(readOnly = true)
public class ParticipantServiceImpl implements ParticipantService {
	
	private ParticipantDao participantDao;
	
	@Autowired
	public void setParticipantDao(ParticipantDao participantDao) {
		this.participantDao = participantDao;
	}

	@Override
	public List<Participant> getAll() {
		try {
			return participantDao.getAll();
		} catch (DaoException e) {
			throw new ServiceException("Unable to get participants list", e);
		}
	}
	
	@Override
	public List<Student> getAllStudents() {
		try {
			return participantDao.getAllStudents();
		} catch (DaoException e) {
			throw new ServiceException("Unable to get students list", e);
		}
	}

	@Override
	public List<Teacher> getAllTeachers() {
		try {
			return participantDao.getAllTeachers();
		} catch (DaoException e) {
			throw new ServiceException("Unable to get teachers list", e);
		}
	}

	@Override
	public Participant getById(int id) {
		validateId(id);
		
		try {
			return participantDao.getById(id);
		} catch (EntityNotFoundDaoException e) {
			String message = String.format("Participant with id = %d not found", id);
			throw new EntityNotFoundServiceException(message);
		} catch (DaoException e) {
			String message = String.format("Unable to get Participant with id = %d", id);
			throw new ServiceException(message, e);
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void add(Participant participant) {
		validateParticipant(participant);
		
		try {
			participantDao.add(participant);
		} catch (DaoException e) {
			String message = String.format("Unable to add Participant: %s", participant);
			throw new ServiceException(message, e);
		}
	}
	
	@Override
	@Transactional(readOnly = false)
	public void update(Participant participant) {
		validateParticipant(participant);
		
		try {
			participantDao.update(participant);
		} catch (DaoException e) {
			String message = String.format("Unable to update Participant: %s", participant);
			throw new ServiceException(message, e);
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void removeById(int id) {
		validateId(id);
		
		try {
			participantDao.removeById(id);
		} catch (DataAccessException e) {
			String message = String.format("Unable to remove Participant with id = %d", id);
			throw new ServiceException(message, e);
		}
	}
	
	@Override
	@Transactional(readOnly = false)
	public void addParticipantCourseById(int participantId, int courseId) {
		validateId(participantId);
		validateId(courseId);
		
		try {
			participantDao.addParticipantCourseById(participantId, courseId);
		} catch (DataAccessException e) {
			String message = String.format("Unable to add Course with id = %d to Participant with id = %d", 
					courseId, participantId);
			throw new ServiceException(message, e);
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void removeParticipantCourseById(int participantId, int courseId) {
		validateId(participantId);
		validateId(courseId);
		
		try {
			participantDao.removeParticipantCourseById(participantId, courseId);
		} catch (DataAccessException e) {
			String message = String.format("Unable to remove Course with id = %d for Participant with id = %d", 
					courseId, participantId);
			throw new ServiceException(message, e);
		}
	}
	
	private void validateId(int id) {
		if (id <= 0) {
			String message = String.format("ID must be a positive integer value. Actually, ID was %d", id);
			throw new ValidationException(message);
		}
	}
	
	private void validateParticipant(Participant participant) {
		if (participant == null) {
			String message = String.format("Participant must not be null. Actually, Participant was %s", participant);
			throw new ValidationException(message);
		}
	}

}
