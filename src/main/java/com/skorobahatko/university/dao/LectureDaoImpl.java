package com.skorobahatko.university.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.skorobahatko.university.dao.exception.DaoException;
import com.skorobahatko.university.dao.exception.EntityNotFoundDaoException;
import com.skorobahatko.university.domain.Lecture;

public class LectureDaoImpl implements LectureDao {
	
	private static final Logger logger = LoggerFactory.getLogger(LectureDaoImpl.class);

	private static final String LECTURE_ID = "lecture_id";
	private static final String COURSE_ID = "course_id";
	private static final String LECTURE_NAME = "lecture_name";
	private static final String LECTURE_DATE = "lecture_date";
	private static final String LECTURE_START_TIME = "lecture_start_time";
	private static final String LECTURE_END_TIME = "lecture_end_time";
	private static final String LECTURE_ROOM_NUMBER = "lecture_room_number";
	
	private static final String GET_ALL_SQL = "SELECT * FROM lectures";

	private static final String GET_BY_ID_SQL = "SELECT * FROM lectures WHERE lecture_id = ?";

	private static final String GET_BY_COURSE_ID_SQL = "SELECT * FROM lectures WHERE course_id = ?";

	private static final String ADD_SQL = "INSERT INTO lectures "
			+ "(course_id, lecture_name, lecture_date, lecture_start_time, lecture_end_time, lecture_room_number) "
			+ "VALUES (?, ?, ?, ?, ?, ?) RETURNING lecture_id;";

	private static final String REMOVE_BY_ID_SQL = "DELETE FROM lectures WHERE lecture_id = ?;";

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public List<Lecture> getAll() {
		logger.debug("Retrieving the lectures list");
		
		try {
			List<Lecture> result = jdbcTemplate.query(GET_ALL_SQL, 
					(rs, rowNum) -> {
						int lectureId = rs.getInt(LECTURE_ID);
						int courseId = rs.getInt(COURSE_ID);
						String lectureName = rs.getString(LECTURE_NAME);
						LocalDate lectureDate = rs.getDate(LECTURE_DATE).toLocalDate();
						LocalTime lectureStartTime = rs.getTime(LECTURE_START_TIME).toLocalTime();
						LocalTime lectureEndTime = rs.getTime(LECTURE_END_TIME).toLocalTime();
						int lectureRoomNumber = rs.getInt(LECTURE_ROOM_NUMBER);
						return new Lecture(lectureId, lectureName, courseId, lectureDate, lectureStartTime,
								lectureEndTime, lectureRoomNumber);
			});
			
			logger.debug("Retrieved {} lectures", result.size());
			
			return result;
		} catch (DataAccessException e) {
			throw new DaoException("Unable to get lectures", e);
		}
	}

	@Override
	public Lecture getById(int id) {
		logger.debug("Retrieving Lecture with id = {}", id);
		
		try {
			Lecture result = jdbcTemplate.queryForObject(GET_BY_ID_SQL, new Object[] {id}, (rs, rowNum) -> {
				int lectureId = rs.getInt(LECTURE_ID);
				int courseId = rs.getInt(COURSE_ID);
				String lectureName = rs.getString(LECTURE_NAME);
				LocalDate lectureDate = rs.getDate(LECTURE_DATE).toLocalDate();
				LocalTime lectureStartTime = rs.getTime(LECTURE_START_TIME).toLocalTime();
				LocalTime lectureEndTime = rs.getTime(LECTURE_END_TIME).toLocalTime();
				int lectureRoomNumber = rs.getInt(LECTURE_ROOM_NUMBER);
				
				return new Lecture(lectureId, lectureName, courseId, lectureDate, lectureStartTime,
						lectureEndTime, lectureRoomNumber);
			});
			
			logger.debug("Lecture with id = {} successfully retrieved", id);
			
			return result;
		} catch (EmptyResultDataAccessException e) {
			String message = String.format("Lecture with id = %d not found", id);
			throw new EntityNotFoundDaoException(message, e);
		} catch (DataAccessException e) {
			String message = String.format("Unable to get Lecture with id = %d", id);
			throw new DaoException(message, e);
		}
	}

	@Override
	public List<Lecture> getByCourseId(int courseId) {
		logger.debug("Retrieving Lecture for Course with id = {}", courseId);
		
		try {
			List<Lecture> result = jdbcTemplate.query(GET_BY_COURSE_ID_SQL, ps -> ps.setInt(1, courseId), (rs, rowNum) -> {
				int lectureId = rs.getInt(LECTURE_ID);
				String lectureName = rs.getString(LECTURE_NAME);
				LocalDate lectureDate = rs.getDate(LECTURE_DATE).toLocalDate();
				LocalTime lectureStartTime = rs.getTime(LECTURE_START_TIME).toLocalTime();
				LocalTime lectureEndTime = rs.getTime(LECTURE_END_TIME).toLocalTime();
				int lectureRoomNumber = rs.getInt(LECTURE_ROOM_NUMBER);
				return new Lecture(lectureId, lectureName, courseId, lectureDate, lectureStartTime, lectureEndTime,
						lectureRoomNumber);
			});
			
			logger.debug("Lectures list for Course with id = {} successfully retrieved", courseId);
			
			return result;
		} catch (DataAccessException e) {
			String message = String.format("Unable to get lectures with Course id = %d", courseId);
			throw new DaoException(message, e);
		}
	}
	
	@Override
	public void addAll(List<Lecture> lectures) {
		lectures.forEach(this::add);
	}

	@Override
	public void add(Lecture lecture) {
		logger.debug("Adding Lecture: {}", lecture);
		
		try {
			int lectureId = jdbcTemplate.queryForObject(ADD_SQL, Long.class, lecture.getCourseId(), lecture.getName(),
					lecture.getDate(), lecture.getStartTime(), lecture.getEndTime(), lecture.getRoomNumber()).intValue();
			lecture.setId(lectureId);
		} catch (DataAccessException e) {
			String message = String.format("Unable to add Lecture: %s", lecture);
			throw new DaoException(message, e);
		}
		
		logger.debug("Successfully added Lecture {}", lecture);
	}

	@Override
	public void removeById(int id) {
		logger.debug("Removing Lecture with id = {}", id);
		
		int affectedRows = 0;
		
		try {
			affectedRows = jdbcTemplate.update(REMOVE_BY_ID_SQL, id);
		} catch (DataAccessException e) {
			String message = String.format("Unable to remove Lecture with id = %d", id);
			throw new DaoException(message, e);
		}
		
		if (affectedRows == 0) {
			String message = String.format("Lecture with id = %d not found", id);
			throw new EntityNotFoundDaoException(message);
		}
		
		logger.debug("Lecture with id = {} successfully removed", id);
	}

}
