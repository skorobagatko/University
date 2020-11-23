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

public class CourseDao {
	
	private static final String COURSE_ID = "course_id";
	private static final String COURSE_NAME = "course_name";

	private static final String GET_ALL_COURSES = "SELECT * FROM courses";

	private static final String GET_COURSE_BY_ID = "SELECT * FROM courses WHERE course_id = ?";
	
	private static final String GET_COURSES_BY_PARTICIPANT_ID = 
			"SELECT courses.course_id, courses.course_name FROM courses "
			+ "INNER JOIN participants_courses ON participants_courses.course_id = courses.course_id "
			+ "WHERE participants_courses.participant_id = ?;";

	private static final String ADD_COURSE = "INSERT INTO courses (course_name) VALUES (?);";

	private static final String REMOVE_COURSE_BY_ID = "DELETE FROM courses WHERE course_id = ?;";

	private JdbcTemplate jdbcTemplate;
	private LectureDao lectureDao;

	public CourseDao(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		lectureDao = new LectureDao(dataSource);
	}

	public List<Course> getAll() {
		return jdbcTemplate.query(GET_ALL_COURSES, 
				(rs, rowNum) -> {
					int courseId = rs.getInt(COURSE_ID);
					String courseName = rs.getString(COURSE_NAME);
					List<Lecture> courseLectures = lectureDao.getByCourseId(courseId);
					return new Course(courseId, courseName, courseLectures);
		});
	}
	
	public List<Course> getAllByParticipantId(int participantId) {
		return jdbcTemplate.query(
				GET_COURSES_BY_PARTICIPANT_ID, 
				ps -> ps.setInt(1, participantId),
				(rs, rowNum) -> {
					int courseId = rs.getInt(COURSE_ID);
					String courseName = rs.getString(COURSE_NAME);
					List<Lecture> courseLectures = lectureDao.getByCourseId(courseId);
					return new Course(courseId, courseName, courseLectures);
				}
		);
	}

	public Optional<Course> getById(int id) {
		return jdbcTemplate.query(con -> con.prepareStatement(GET_COURSE_BY_ID), 
				ps -> ps.setInt(1, id), 
				rs -> {
					if (rs.next()) {
						int courseId = rs.getInt(COURSE_ID);
						String courseName = rs.getString(COURSE_NAME);
						List<Lecture> courseLectures = lectureDao.getByCourseId(courseId);
						return Optional.of(new Course(courseId, courseName, courseLectures));
					} else {
						return Optional.empty();
					}
				});
	}

	public void add(Course course) {
		int courseId = insertCourse(course);
		course.setId(courseId);
		
		List<Lecture> lectures = course.getLectures();
		if(!lectures.isEmpty()) {
			setCourseIdToLectures(courseId, lectures);
			lectureDao.addAll(lectures);
		}
	}

	public void removeById(int id) {
		jdbcTemplate.update(REMOVE_COURSE_BY_ID, id);
	}
	
	private int insertCourse(Course course) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
	        PreparedStatement ps = connection.prepareStatement(ADD_COURSE, Statement.RETURN_GENERATED_KEYS);
	  	    ps.setString(1, course.getName());
	  	    return ps;
		}, keyHolder);
		
		return (int) keyHolder.getKeys().get(COURSE_ID);
	}

	private void setCourseIdToLectures(int courseId, List<Lecture> lectures) {
		lectures.forEach(lecture -> lecture.setCourseId(courseId));
	}
	
}
