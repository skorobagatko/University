package com.skorobahatko.university.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.skorobahatko.university.domain.Course;
import com.skorobahatko.university.domain.Lecture;
import com.skorobahatko.university.domain.Participant;
import com.skorobahatko.university.domain.Student;
import com.skorobahatko.university.domain.Teacher;

public class ParticipantDao {
	
	private static final String PARTICIPANT_ID = "participant_id";
	private static final String FIRST_NAME = "first_name";
	private static final String LAST_NAME = "last_name";
	private static final String ROLE_NAME = "role_name";
	private static final String COURSE_ID = "course_id";
	private static final String COURSE_NAME = "course_name";
	
	private static final String GET_ALL_SQL = 
			"SELECT participant_id, first_name, last_name, participants.role_id, role_name "
			+ "FROM participants INNER JOIN participant_roles ON participant_roles.role_id = participants.role_id;";
	
	private static final String GET_BY_ID_SQL = 
			"SELECT participants.first_name, participants.last_name, participant_roles.role_name FROM participants "
			+ "INNER JOIN participant_roles ON participant_roles.role_id = participants.role_id "
			+ "WHERE participants.participant_id = ?;";
	
	private static final String ADD_SQL = "INSERT INTO participants (first_name, last_name, role_id) "
			+ "VALUES (?, ?, ?);";
	
	private static final String ADD_PARTICIPANT_COURSE_PAIR_SQL = "INSERT INTO participants_courses "
			+ "(participant_id, course_id) VALUES (?, ?);";
	
	private static final String REMOVE_BY_ID_SQL = "DELETE FROM participants WHERE participant_id = ?;";
	
	private static final String GET_PARTICIPANT_ROLE_ID_SQL = "SELECT role_id FROM participant_roles "
			+ "WHERE role_name = ?;";
	
	private static final String GET_COURSES_BY_PARTICIPANT_ID_SQL = 
			"SELECT courses.course_id, courses.course_name FROM courses "
			+ "INNER JOIN participants_courses ON participants_courses.course_id = courses.course_id "
			+ "WHERE participants_courses.participant_id = ?;";
	
	private JdbcTemplate jdbcTemplate;
	private CourseDao courseDao;
	private LectureDao lectureDao;
	
	public ParticipantDao(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		courseDao = new CourseDao(dataSource);
		lectureDao = new LectureDao(dataSource);
	}
	
	public List<Participant> getAll() {
		return jdbcTemplate.query(GET_ALL_SQL, (rs, rowNum) -> {
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
	}

	public Optional<Participant> getById(int id) {
		return jdbcTemplate.query(GET_BY_ID_SQL, 
				ps -> ps.setInt(1, id),
				rs -> {
					if (rs.next()) {
						String firstName = rs.getString(FIRST_NAME);
						String lastName = rs.getString(LAST_NAME);
						String roleName = rs.getString(ROLE_NAME);
						List<Course> courses = courseDao.getAllByParticipantId(id);
						
						Participant participant = getParticipantByRoleName(roleName);
						participant.setId(id);
						participant.setFirstName(firstName);
						participant.setLastName(lastName);
						participant.setCourses(courses);
						
						return Optional.of(participant);
					} else {
						return Optional.empty();
					}
				}
		);
	}
	
	public void add(Participant participant) {
		checkForNull(participant);
		
		int participantId = insertParticipant(participant);
		participant.setId(participantId);
		
		insertParticipantCoursePairs(participant);
	}
	
	public void removeById(int id) {
		jdbcTemplate.update(REMOVE_BY_ID_SQL, id);
	}
	
	private int insertParticipant(Participant participant) {
		int roleId = getParticipantRoleId(participant);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
	        PreparedStatement ps = connection.prepareStatement(ADD_SQL, Statement.RETURN_GENERATED_KEYS);
	  	    ps.setString(1, participant.getFirstName());
	  	    ps.setString(2, participant.getLastName());
	  	    ps.setInt(3, roleId);
	  	    return ps;
		}, keyHolder);
		
		return (int) keyHolder.getKeys().get(PARTICIPANT_ID);
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
		return jdbcTemplate.query(GET_PARTICIPANT_ROLE_ID_SQL,
				ps -> {
					String roleName = null;
					if (participant instanceof Student) {
						roleName = "Student";
					} else if (participant instanceof Teacher) {
						roleName = "Teacher";
					}
					ps.setString(1, roleName);
				},
				rs -> {
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
			throw new DaoException("Can't find participant role with name '" + roleName + "'");
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
