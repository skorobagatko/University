package com.skorobahatko.university.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.skorobahatko.university.dao.exception.DaoException;
import com.skorobahatko.university.dao.exception.EntityNotFoundDaoException;
import com.skorobahatko.university.domain.Course;
import com.skorobahatko.university.domain.Lecture;

public class CourseDaoImpl implements CourseDao {

	private static final Logger logger = LoggerFactory.getLogger(CourseDaoImpl.class);

	private static final String COURSE_ID = "course_id";
	private static final String COURSE_NAME = "course_name";

	private static final String GET_ALL_SQL = "SELECT * FROM courses";

	private static final String GET_BY_ID_SQL = "SELECT * FROM courses WHERE course_id = ?";

	private static final String GET_ALL_BY_PARTICIPANT_ID_SQL = 
			"SELECT courses.course_id, courses.course_name FROM courses "
			+ "INNER JOIN participants_courses ON participants_courses.course_id = courses.course_id "
			+ "WHERE participants_courses.participant_id = ?;";

	private static final String ADD_SQL = "INSERT INTO courses (course_name) VALUES (?) RETURNING course_id;";
	
	private static final String UPDATE_SQL = "UPDATE courses SET course_name = ? WHERE course_id = ?;";

	private static final String REMOVE_BY_ID_SQL = "DELETE FROM courses WHERE course_id = ?;";

	private JdbcTemplate jdbcTemplate;
	private LectureDao lectureDao;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void setLectureDao(LectureDao lectureDao) {
		this.lectureDao = lectureDao;
	}

	@Override
	public List<Course> getAll() {
		logger.debug("Retrieving courses list");
		
		try {
			List<Course> result = jdbcTemplate.query(GET_ALL_SQL, (rs, rowNum) -> {
				int courseId = rs.getInt(COURSE_ID);
				String courseName = rs.getString(COURSE_NAME);
				List<Lecture> lectures = lectureDao.getByCourseId(courseId);
				return new Course(courseId, courseName, lectures);
			});
			
			logger.debug("Courses list successfully retrieved. Retrieved {} records", result.size());
			
			return result;
		} catch (DataAccessException e) {
			throw new DaoException("Unable to get courses", e);
		}
	}

		try {
			List<Course> result = jdbcTemplate.query(GET_ALL_BY_PARTICIPANT_ID_SQL, ps -> ps.setInt(1, participantId),
					(rs, rowNum) -> {
						int courseId = rs.getInt(COURSE_ID);
						String courseName = rs.getString(COURSE_NAME);
						List<Lecture> lectures = lectureDao.getByCourseId(courseId);
						return new Course(courseId, courseName, lectures);
					});
			
			logger.debug("Courses list for participant with id = {} successfully retrieved from the database", participantId);
			
			return result;
		} catch (DataAccessException e) {
			String message = String.format("Unable to get courses for participant with id = %d", participantId);
			throw new DaoException(message, e);
		}
	}

	@Override
	public Course getById(int id) {
		logger.debug("Retrieving Course with id = {}", id);
		
		try {
			Course result = jdbcTemplate.queryForObject(GET_BY_ID_SQL, new Object[] {id}, (rs, rowNum) -> {
				int courseId = rs.getInt(COURSE_ID);
				String courseName = rs.getString(COURSE_NAME);
				List<Lecture> lectures = lectureDao.getByCourseId(courseId);
				
				return new Course(courseId, courseName, lectures);
			});
			
			logger.debug("Course with id = {} successfully retrieved", id);
			
			return result;
		} catch (EmptyResultDataAccessException e) {
			String message = String.format("Course with id = %d not found", id);
			throw new EntityNotFoundDaoException(message, e);
		} catch (DataAccessException e) {
			String message = String.format("Unable to retrieve Course with id = %d", id);
			throw new DaoException(message, e);
		}
	}

	@Override
	public void add(Course course) {
		logger.debug("Adding Course: {}", course);
		
		checkForNull(course);
		
		try {
			int courseId = jdbcTemplate.queryForObject(ADD_SQL, Long.class, course.getName()).intValue();
			course.setId(courseId);

			List<Lecture> lectures = course.getLectures();
			if (!lectures.isEmpty()) {
				setCourseIdToLectures(courseId, lectures);
				lectureDao.addAll(lectures);
			}
		} catch (DataAccessException e) {
			String message = String.format("Unable to add Course: %s", course);
			throw new DaoException(message, e);
		}
		
		logger.debug("Successfully added Course {}", course);
	}
	
	@Override
	public void update(Course course) {
		logger.debug("Updating Course: {}", course);
		
		checkForNull(course);
		
		try {
			jdbcTemplate.update(UPDATE_SQL, course.getName(), course.getId());
		} catch (DataAccessException e) {
			String message = String.format("Unable to update Course: %s", course);
			throw new DaoException(message, e);
		}
		
		logger.debug("Successfully updated Course {}", course);
	}

	@Override
	public void removeById(int id) {
		logger.debug("Removing Course with id = {}", id);
		
		int affectedRows = 0;
		
		try {
			affectedRows = jdbcTemplate.update(REMOVE_BY_ID_SQL, id);
		} catch (DataAccessException e) {
			String message = String.format("Unable to remove Course with id = %d", id);
			throw new DaoException(message, e);
		}
		
		if (affectedRows == 0) {
			String message = String.format("Course with id = %d not found", id);
			throw new EntityNotFoundDaoException(message);
		}
		
		logger.debug("Course with id = {} successfully removed", id);
	}
	
	private void checkForNull(Course course) {
		if (course == null) {
			throw new DaoException("The input argument must no be null");
		}
	}

	private void setCourseIdToLectures(int courseId, List<Lecture> lectures) {
		lectures.forEach(lecture -> lecture.setCourseId(courseId));
	}

}
