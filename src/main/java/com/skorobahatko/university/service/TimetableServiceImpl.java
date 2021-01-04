package com.skorobahatko.university.service;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.skorobahatko.university.dao.TimetableDao;
import com.skorobahatko.university.dao.exception.DaoException;
import com.skorobahatko.university.dao.exception.EntityNotFoundDaoException;
import com.skorobahatko.university.domain.Timetable;
import com.skorobahatko.university.service.exception.EntityNotFoundServiceException;
import com.skorobahatko.university.service.exception.ServiceException;
import com.skorobahatko.university.service.exception.ValidationException;

public class TimetableServiceImpl implements TimetableService {
	
	private TimetableDao timetableDao;
	
	public void setTimetableDao(TimetableDao timetableDao) {
		this.timetableDao = timetableDao;
	}

	@Override
	public List<Timetable> getAll() {
		try {
			return timetableDao.getAll();
		} catch (DaoException e) {
			throw new ServiceException("Unable to get timetables list", e);
		}
	}

	@Override
	public Timetable getById(int id) {
		validateId(id);
		
		try {
			return timetableDao.getById(id);
		} catch (EntityNotFoundDaoException e) {
			String message = String.format("Timetable with id = %d not found", id);
			throw new EntityNotFoundServiceException(message);
		} catch (DaoException e) {
			String message = String.format("Unable to get Timetable with id = %d", id);
			throw new ServiceException(message, e);
		}
	}
	
	@Override
	public Timetable getByParticipantId(int participantId) {
		validateId(participantId);
		
		try {
			return timetableDao.getByParticipantId(participantId);
		} catch (EntityNotFoundDaoException e) {
			String message = String.format("Timetable for Participant with id = %d not found", participantId);
			throw new EntityNotFoundServiceException(message);
		} catch (DaoException e) {
			String message = String.format("Unable to get Timetable for Participant with id = %d", participantId);
			throw new ServiceException(message, e);
		}
	}

	@Override
	public void add(Timetable timetable) {
		validateLecture(timetable);
		
		try {
			timetableDao.add(timetable);
		} catch (DaoException e) {
			String message = String.format("Unable to add the Timetable: %s", timetable);
			throw new ServiceException(message, e);
		}
	}
	
	@Override
	public void update(Timetable timetable) {
		validateLecture(timetable);
		
		try {
			timetableDao.update(timetable);
		} catch (DaoException e) {
			String message = String.format("Unable to update Timetable: %s", timetable);
			throw new ServiceException(message, e);
		}
	}

	@Override
	public void removeById(int id) {
		validateId(id);
		
		try {
			timetableDao.removeById(id);
		} catch (DataAccessException e) {
			String message = String.format("Unable to remove Timetable with id = %d", id);
			throw new ServiceException(message, e);
		}
	}
	
	private void validateId(int id) {
		if (id <= 0) {
			String message = String.format("ID must be a positive integer value. Actually, ID was %d", id);
			throw new ValidationException(message);
		}
	}
	
	private void validateLecture(Timetable timetable) {
		if (timetable == null) {
			String message = String.format("Timetable must not be null. Actually, Timetable was %s", timetable);
			throw new ValidationException(message);
		}
	}

}
