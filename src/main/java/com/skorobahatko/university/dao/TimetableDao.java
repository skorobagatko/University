package com.skorobahatko.university.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.skorobahatko.university.domain.Participant;
import com.skorobahatko.university.domain.Timetable;

public class TimetableDao {
	
	private static final String TIMETABLE_ID = "timetable_id";
	private static final String PARTICIPANT_ID = "participant_id";
	private static final String TIMETABLE_START_DATE = "timetable_start_date";
	private static final String TIMETABLE_END_DATE = "timetable_end_date";
	
	private static final String GET_ALL_SQL = "SELECT * FROM timetables;";
	
	private static final String GET_BY_ID_SQL = "SELECT * FROM timetables WHERE timetable_id = ?;";
	
	private static final String ADD_SQL = "INSERT INTO timetables "
			+ "(participant_id, timetable_start_date, timetable_end_date) "
			+ "VALUES (?, ?, ?);";
	
	private static final String REMOVE_BY_ID_SQL = "DELETE FROM timetables WHERE timetable_id = ?;";
	
	private JdbcTemplate jdbcTemplate;
	private ParticipantDao participantDao;
	
	public TimetableDao(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		participantDao = new ParticipantDao(dataSource);
	}
	
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
	
	public void add(Timetable timetable) {
		checkForNull(timetable);
		
		int timetableId = insertTimetable(timetable);
		
		timetable.setId(timetableId);
	}
	
	public void removeById(int id) {
		jdbcTemplate.update(REMOVE_BY_ID_SQL, id);
	}
	
	private int insertTimetable(Timetable timetable) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
	        PreparedStatement ps = connection.prepareStatement(ADD_SQL, Statement.RETURN_GENERATED_KEYS);
	  	    ps.setInt(1, timetable.getParticipant().getId());
	  	    ps.setDate(2, Date.valueOf(timetable.getStartDate()));
	  	    ps.setDate(3, Date.valueOf(timetable.getEndDate()));
	  	    return ps;
		}, keyHolder);
		
		return (int) keyHolder.getKeys().get(TIMETABLE_ID);
	}
	
	private void checkForNull(Timetable timetable) {
		if (timetable == null) {
			throw new DaoException("The input argument must no be null");
		}
	}

}
