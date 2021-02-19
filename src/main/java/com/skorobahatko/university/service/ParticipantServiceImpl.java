package com.skorobahatko.university.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skorobahatko.university.domain.Participant;
import com.skorobahatko.university.domain.Student;
import com.skorobahatko.university.domain.Teacher;
import com.skorobahatko.university.repository.ParticipantRepository;
import com.skorobahatko.university.service.exception.EntityNotFoundServiceException;
import com.skorobahatko.university.service.exception.ServiceException;
import com.skorobahatko.university.service.exception.ValidationException;

@Service("participantService")
@Transactional(readOnly = true)
public class ParticipantServiceImpl implements ParticipantService {
	
	private ParticipantRepository participantRepository;
	
	@Autowired
	public void setParticipantRepository(ParticipantRepository participantRepository) {
		this.participantRepository = participantRepository;
	}

	@Override
	public List<Participant> getAll() {
		try {
			return participantRepository.findAll();
		} catch (DataAccessException e) {
			throw new ServiceException("Unable to get participants list", e);
		}
	}
	
	@Override
	public List<Student> getAllStudents() {
		try {
			return participantRepository.findAllStudents();
		} catch (DataAccessException e) {
			throw new ServiceException("Unable to get students list", e);
		}
	}

	@Override
	public List<Teacher> getAllTeachers() {
		try {
			return participantRepository.findAllTeachers();
		} catch (DataAccessException e) {
			throw new ServiceException("Unable to get teachers list", e);
		}
	}

	@Override
	public Participant getById(int id) {
		validateId(id);
		
		try {
			return participantRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			String message = String.format("Participant with id = %d not found", id);
			throw new EntityNotFoundServiceException(message);
		} catch (DataAccessException e) {
			String message = String.format("Unable to get Participant with id = %d", id);
			throw new ServiceException(message, e);
		}
	}

	@Override
	@Transactional
	public Participant add(Participant participant) {
		validateParticipant(participant);
		
		try {
			return participantRepository.save(participant);
		} catch (DataAccessException e) {
			String message = String.format("Unable to add Participant: %s", participant);
			throw new ServiceException(message, e);
		}
	}
	
	@Override
	@Transactional
	public void update(Participant participant) {
		validateParticipant(participant);
		
		try {
			participantRepository.save(participant);
		} catch (DataAccessException e) {
			String message = String.format("Unable to update Participant: %s", participant);
			throw new ServiceException(message, e);
		}
	}

	@Override
	@Transactional
	public void removeById(int id) {
		validateId(id);
		
		try {
			participantRepository.deleteById(id);
		} catch (DataAccessException e) {
			String message = String.format("Unable to remove Participant with id = %d", id);
			throw new ServiceException(message, e);
		}
	}
	
	@Override
	@Transactional
	public void addParticipantCourseById(int participantId, int courseId) {
		validateId(participantId);
		validateId(courseId);
		
		try {
			participantRepository.addParticipantCourseById(participantId, courseId);
		} catch (DataAccessException e) {
			String message = String.format("Unable to add Course with id = %d to Participant with id = %d", 
					courseId, participantId);
			throw new ServiceException(message, e);
		}
	}

	@Override
	@Transactional
	public void removeParticipantCourseById(int participantId, int courseId) {
		validateId(participantId);
		validateId(courseId);
		
		try {
			participantRepository.deleteParticipantCourseById(participantId, courseId);
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
