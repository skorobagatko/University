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
import com.skorobahatko.university.domain.Participant;
import com.skorobahatko.university.domain.Student;
import com.skorobahatko.university.domain.Teacher;

public class ParticipantDaoImpl implements ParticipantDao {
	
	private static final Logger logger = LoggerFactory.getLogger(ParticipantDaoImpl.class);

	private static final String PARTICIPANT_ID = "participant_id";
	private static final String FIRST_NAME = "first_name";
	private static final String LAST_NAME = "last_name";
	private static final String ROLE_NAME = "role_name";
	private static final String COURSE_ID = "course_id";
	private static final String COURSE_NAME = "course_name";

	private static final String GET_ALL_SQL = "SELECT participant_id, first_name, last_name, participants.role_id, role_name "
			+ "FROM participants INNER JOIN participant_roles ON participant_roles.role_id = participants.role_id;";

	private static final String GET_BY_ID_SQL = "SELECT participants.first_name, participants.last_name, participant_roles.role_name FROM participants "
			+ "INNER JOIN participant_roles ON participant_roles.role_id = participants.role_id "
			+ "WHERE participants.participant_id = ?;";

	private static final String ADD_SQL = "INSERT INTO participants (first_name, last_name, role_id) "
			+ "VALUES (?, ?, ?) RETURNING participant_id;";

	private static final String ADD_PARTICIPANT_COURSE_PAIR_SQL = "INSERT INTO participants_courses "
			+ "(participant_id, course_id) VALUES (?, ?);";

	private static final String REMOVE_BY_ID_SQL = "DELETE FROM participants WHERE participant_id = ?;";

	private static final String GET_PARTICIPANT_ROLE_ID_SQL = "SELECT role_id FROM participant_roles "
			+ "WHERE role_name = ?;";

	private static final String GET_COURSES_BY_PARTICIPANT_ID_SQL = "SELECT courses.course_id, courses.course_name FROM courses "
			+ "INNER JOIN participants_courses ON participants_courses.course_id = courses.course_id "
			+ "WHERE participants_courses.participant_id = ?;";

	private JdbcTemplate jdbcTemplate;
	private CourseDao courseDao;
	private LectureDao lectureDao;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void setCourseDao(CourseDao courseDao) {
		this.courseDao = courseDao;
	}

	public void setLectureDao(LectureDao lectureDao) {
		this.lectureDao = lectureDao;
	}

	@Override
	public List<Participant> getAll() {
		logger.debug("Retrieving the participants list");
		
		try {
			List<Participant> result = jdbcTemplate.query(GET_ALL_SQL, (rs, rowNum) -> {
				int id = rs.getInt(PARTICIPANT_ID);
				String firstName = rs.getString(FIRST_NAME);
				String lastName = rs.getString(LAST_NAME);
				String roleName = rs.getString(ROLE_NAME);
				List<Course> courses = getParticipantCoursesByParticipantId(id);

				Participant participant = getParticipantByRoleName(roleName);
				participant.setId(id);
				participant.setFirstName(firstName);
				participant.setLastName(lastName);
				participant.setCourses(courses);

				return participant;
			});
			
			logger.debug("Retrieved {} participants", result.size());
			
			return result;
		} catch (DataAccessException e) {
			throw new DaoException("Unable to get participants", e);
		}
	}

	@Override
	public Participant getById(int id) {
		logger.debug("Retrieving Participant with id = {}", id);
		
		try {
			Participant result = jdbcTemplate.queryForObject(GET_BY_ID_SQL, new Object[] {id}, (rs, rowNum) -> {
				String firstName = rs.getString(FIRST_NAME);
				String lastName = rs.getString(LAST_NAME);
				String roleName = rs.getString(ROLE_NAME);
				List<Course> courses = courseDao.getAllByParticipantId(id);

				Participant participant = getParticipantByRoleName(roleName);
				participant.setId(id);
				participant.setFirstName(firstName);
				participant.setLastName(lastName);
				participant.setCourses(courses);
				
				return participant;
			});
			
			logger.debug("Participant with id = {} successfully retrieved", id);
			
			return result;
		} catch (EmptyResultDataAccessException e) {
			String message = String.format("Participant with id = %d not found", id);
			throw new EntityNotFoundDaoException(message, e);
		} catch (DataAccessException e) {
			String message = String.format("Unable to get Participant with id = %d", id);
			throw new DaoException(message, e);
		}
	}

	@Override
	public void add(Participant participant) {
		logger.debug("Adding Participant: {}", participant);
		
		checkForNull(participant);

		try {
			int participantId = insertParticipant(participant);
			
			participant.setId(participantId);

			insertParticipantCoursePairs(participant);
		} catch (DataAccessException e) {
			String message = String.format("Unable to add Participant: %s", participant);
			throw new DaoException(message, e);
		}
		
		logger.debug("Successfully added Participant {}", participant);
	}

	@Override
	public void removeById(int id) {
		logger.debug("Removing Participant with id = {}", id);
		
		int affectedRows = 0;
		
		try {
			affectedRows = jdbcTemplate.update(REMOVE_BY_ID_SQL, id);
		} catch (DataAccessException e) {
			String message = String.format("Unable to remove Participant with id = %d", id);
			throw new DaoException(message, e);
		}
		
		if (affectedRows == 0) {
			String message = String.format("Participant with id = %d not found", id);
			throw new EntityNotFoundDaoException(message);
		}
		
		logger.debug("Participant with id = {} successfully removed", id);
	}

	private int insertParticipant(Participant participant) {
		int roleId = getParticipantRoleId(participant);
		return jdbcTemplate
				.queryForObject(ADD_SQL, Long.class, participant.getFirstName(), participant.getLastName(), roleId)
				.intValue();
	}

	private void insertParticipantCoursePairs(Participant participant) {
		List<Course> courses = participant.getCourses();
		for (Course course : courses) {
			jdbcTemplate.update(ADD_PARTICIPANT_COURSE_PAIR_SQL, participant.getId(), course.getId());
		}
	}

	private void checkForNull(Participant participant) {
		if (participant == null) {
			throw new DaoException("The input argument must no be null");
		}
	}

	private int getParticipantRoleId(Participant participant) {
		return jdbcTemplate.query(GET_PARTICIPANT_ROLE_ID_SQL, ps -> {
			String roleName = null;
			if (participant instanceof Student) {
				roleName = "Student";
			} else if (participant instanceof Teacher) {
				roleName = "Teacher";
			}
			ps.setString(1, roleName);
		}, rs -> {
			rs.next();
			return rs.getInt("role_id");
		});
	}

	private Participant getParticipantByRoleName(String roleName) {
		if (roleName.equalsIgnoreCase("Teacher")) {
			return new Teacher();
		} else if (roleName.equalsIgnoreCase("Student")) {
			return new Student();
		} else {
			throw new DaoException("Unable to get Participant role with name '" + roleName + "'");
		}
	}

	private List<Course> getParticipantCoursesByParticipantId(int participantId) {
		return jdbcTemplate.query(GET_COURSES_BY_PARTICIPANT_ID_SQL, ps -> ps.setInt(1, participantId),
				(rs, rowNum) -> {
					int courseId = rs.getInt(COURSE_ID);
					String courseName = rs.getString(COURSE_NAME);
					List<Lecture> courseLectures = lectureDao.getByCourseId(courseId);
					return new Course(courseId, courseName, courseLectures);
				});
	}

}
