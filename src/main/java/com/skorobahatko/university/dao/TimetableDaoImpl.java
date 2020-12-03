package com.skorobahatko.university.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;

import com.skorobahatko.university.domain.Participant;
import com.skorobahatko.university.domain.Timetable;

public class TimetableDaoImpl implements TimetableDao {
	
	private static final String TIMETABLE_ID = "timetable_id";
	private static final String PARTICIPANT_ID = "participant_id";
	private static final String TIMETABLE_START_DATE = "timetable_start_date";
	private static final String TIMETABLE_END_DATE = "timetable_end_date";
	
	private static final String GET_ALL_SQL = "SELECT * FROM timetables;";
	
	private static final String GET_BY_ID_SQL = "SELECT * FROM timetables WHERE timetable_id = ?;";
	
	private static final String ADD_SQL = "INSERT INTO timetables "
			+ "(participant_id, timetable_start_date, timetable_end_date) "
			+ "VALUES (?, ?, ?) RETURNING timetable_id;";
	
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
		return jdbcTemplate.query(GET_ALL_SQL, (rs, rowNum) -> {
			int timetableId = rs.getInt(TIMETABLE_ID);
			int participantId = rs.getInt(PARTICIPANT_ID);
			LocalDate startDate = rs.getDate(TIMETABLE_START_DATE).toLocalDate();
			LocalDate endDate = rs.getDate(TIMETABLE_END_DATE).toLocalDate();
			
			Optional<Participant> participantOptional = participantDao.getById(participantId);
			if (participantOptional.isEmpty()) {
				throw new DaoException("Can't find the participant with id = " + participantId);
			}
			Participant participant = participantOptional.get();
			
			return new Timetable(timetableId, participant, startDate, endDate);
		});
	}
	
	@Override
	public Optional<Timetable> getById(int id) {
		return jdbcTemplate.query(GET_BY_ID_SQL, 
				ps -> ps.setInt(1, id),
				rs -> {
					if (rs.next()) {
						int timetableId = rs.getInt(TIMETABLE_ID);
						int participantId = rs.getInt(PARTICIPANT_ID);
						LocalDate startDate = rs.getDate(TIMETABLE_START_DATE).toLocalDate();
						LocalDate endDate = rs.getDate(TIMETABLE_END_DATE).toLocalDate();
						
						Optional<Participant> participantOptional = participantDao.getById(participantId);
						if (participantOptional.isEmpty()) {
							throw new DaoException("Can't find the participant with id = " + participantId);
						}
						Participant participant = participantOptional.get();
						
						return Optional.of(new Timetable(timetableId, participant, startDate, endDate));
					} else {
						return Optional.empty();
					}
				}
		);
	}
	
	@Override
	public void add(Timetable timetable) {
		checkForNull(timetable);
		
		int timetableId = insertTimetable(timetable);
		
		timetable.setId(timetableId);
	}
	
	@Override
	public void removeById(int id) {
		jdbcTemplate.update(REMOVE_BY_ID_SQL, id);
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
