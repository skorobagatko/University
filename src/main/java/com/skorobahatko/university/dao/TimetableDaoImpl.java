package com.skorobahatko.university.dao;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.skorobahatko.university.dao.exception.DaoException;
import com.skorobahatko.university.dao.exception.EntityNotFoundDaoException;
import com.skorobahatko.university.domain.Participant;
import com.skorobahatko.university.domain.Timetable;

public class TimetableDaoImpl implements TimetableDao {
	
	private static final Logger logger = LoggerFactory.getLogger(TimetableDaoImpl.class);
	
	private static final String TIMETABLE_ID = "timetable_id";
	private static final String PARTICIPANT_ID = "participant_id";
	private static final String TIMETABLE_START_DATE = "timetable_start_date";
	private static final String TIMETABLE_END_DATE = "timetable_end_date";
	
	private static final String GET_ALL_SQL = "SELECT * FROM timetables;";
	
	private static final String GET_BY_ID_SQL = "SELECT * FROM timetables WHERE timetable_id = ?;";
	
	private static final String GET_BY_PARTICIPANT_ID_SQL = "SELECT * FROM timetables WHERE participant_id = ?;";
	
	private static final String ADD_SQL = "INSERT INTO timetables "
			+ "(participant_id, timetable_start_date, timetable_end_date) "
			+ "VALUES (?, ?, ?) RETURNING timetable_id;";
	
	private static final String UPDATE_SQL = "UPDATE timetables SET timetable_start_date = ?, "
			+ "timetable_end_date = ? WHERE timetable_id = ?;";
	
	private static final String REMOVE_BY_ID_SQL = "DELETE FROM timetables WHERE timetable_id = ?;";
	
	private JdbcTemplate jdbcTemplate;
	private ParticipantDao participantDao;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public void setParticipantDao(ParticipantDao participantDao) {
		this.participantDao = participantDao;
	}
	
	@Override
	public List<Timetable> getAll() {
		logger.debug("Retrieving Timetables list");
		
		try {
			List<Timetable> result = jdbcTemplate.query(GET_ALL_SQL, (rs, rowNum) -> {
				int timetableId = rs.getInt(TIMETABLE_ID);
				int participantId = rs.getInt(PARTICIPANT_ID);
				LocalDate startDate = rs.getDate(TIMETABLE_START_DATE).toLocalDate();
				LocalDate endDate = rs.getDate(TIMETABLE_END_DATE).toLocalDate();
				
				Participant participant = participantDao.getById(participantId);
				
				return new Timetable(timetableId, participant, startDate, endDate);
			});
			
			logger.debug("Retrieved {} Timetables", result.size());
			
			return result;
		} catch (DataAccessException e) {
			throw new DaoException("Unable to get timetables", e);
		}
	}
	
	@Override
	public Timetable getById(int id) {
		logger.debug("Retrieving Timetable with id = {}", id);
		
		try {
			Timetable result = jdbcTemplate.queryForObject(GET_BY_ID_SQL, new Object[] {id}, (rs, rowNum) -> {
				int timetableId = rs.getInt(TIMETABLE_ID);
				int participantId = rs.getInt(PARTICIPANT_ID);
				LocalDate startDate = rs.getDate(TIMETABLE_START_DATE).toLocalDate();
				LocalDate endDate = rs.getDate(TIMETABLE_END_DATE).toLocalDate();
				Participant participant = participantDao.getById(participantId);
				
				return new Timetable(timetableId, participant, startDate, endDate);
			});
			
			logger.debug("Timetable with id = {} successfully retrieved", id);
			
			return result;
		} catch (EmptyResultDataAccessException e) {
			String message = String.format("Timetable with id = %d not found", id);
			throw new EntityNotFoundDaoException(message, e);
		} catch (DataAccessException e) {
			String message = String.format("Unable to get Timetable with id = %d", id);
			throw new DaoException(message, e);
		}
	}
	
	@Override
	public Timetable getByParticipantId(int participantId) {
		logger.debug("Retrieving Timetable for Participant with id = {}", participantId);
		
		try {
			Timetable result = jdbcTemplate.queryForObject(GET_BY_PARTICIPANT_ID_SQL, new Object[] {participantId}, (rs, rowNum) -> {
				int timetableId = rs.getInt(TIMETABLE_ID);
				LocalDate startDate = rs.getDate(TIMETABLE_START_DATE).toLocalDate();
				LocalDate endDate = rs.getDate(TIMETABLE_END_DATE).toLocalDate();
				Participant participant = participantDao.getById(participantId);
				
				return new Timetable(timetableId, participant, startDate, endDate);
			});
			
			logger.debug("Timetable for Participant with id = {} successfully retrieved", participantId);
			
			return result;
		} catch (EmptyResultDataAccessException e) {
			String message = String.format("Timetable for Participant with id = %d not found", participantId);
			throw new EntityNotFoundDaoException(message, e);
		} catch (DataAccessException e) {
			String message = String.format("Unable to get Timetable for Participant  with id = %d", participantId);
			throw new DaoException(message, e);
		}
	}
	
	@Override
	public void add(Timetable timetable) {
		logger.debug("Adding Timetable: {}", timetable);
		
		checkForNull(timetable);
		
		try {
			int timetableId = insertTimetable(timetable);
			
			timetable.setId(timetableId);
		} catch (DataAccessException e) {
			String message = String.format("Unable to add Timetable: %s", timetable);
			throw new DaoException(message, e);
		}
		
		logger.debug("Successfully added Timetable {}", timetable);
	}
	
	@Override
	public void update(Timetable timetable) {
		logger.debug("Updating Timetable: {}", timetable);
		
		checkForNull(timetable);
		
		try {
			jdbcTemplate.update(UPDATE_SQL, timetable.getStartDate(), timetable.getEndDate(), timetable.getId());
		} catch (DataAccessException e) {
			String message = String.format("Unable to update Timetable: %s", timetable);
			throw new DaoException(message, e);
		}
		
		logger.debug("Successfully updated Timetable {}", timetable);
	}
	
	@Override
	public void removeById(int id) {
		logger.debug("Removing Timetable with id = {}", id);
		
		int affectedRows = 0;
		
		try {
			affectedRows = jdbcTemplate.update(REMOVE_BY_ID_SQL, id);
		} catch (DataAccessException e) {
			String message = String.format("Unable to remove Timetable with id = %d", id);
			throw new DaoException(message, e);
		}
		
		if (affectedRows == 0) {
			String message = String.format("Timetable with id = %d not found", id);
			throw new EntityNotFoundDaoException(message);
		}
		
		logger.debug("Timetable with id = {} successfully removed", id);
	}
	
	private int insertTimetable(Timetable timetable) {
		int participantId = timetable.getParticipant().getId();
		LocalDate startDate = timetable.getStartDate();
		LocalDate endDate = timetable.getEndDate();
		
		return jdbcTemplate
				.queryForObject(ADD_SQL, Long.class, participantId, startDate, endDate)
				.intValue();
	}
	
	private void checkForNull(Timetable timetable) {
		if (timetable == null) {
			throw new DaoException("The input argument must no be null");
		}
	}

}
