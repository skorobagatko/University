package com.skorobahatko.university.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;

import com.skorobahatko.university.domain.Course;
import com.skorobahatko.university.domain.Lecture;

public class CourseDaoImpl implements CourseDao {
	
	private static final String COURSE_ID = "course_id";
	private static final String COURSE_NAME = "course_name";

	private static final String GET_ALL_SQL = "SELECT * FROM courses";

	private static final String GET_BY_ID_SQL = "SELECT * FROM courses WHERE course_id = ?";
	
	private static final String GET_ALL_BY_PARTICIPANT_ID_SQL = 
			"SELECT courses.course_id, courses.course_name FROM courses "
			+ "INNER JOIN participants_courses ON participants_courses.course_id = courses.course_id "
			+ "WHERE participants_courses.participant_id = ?;";

	private static final String ADD_SQL = "INSERT INTO courses (course_name) VALUES (?) RETURNING course_id;";

	private static final String REMOVE_BY_ID_SQL = "DELETE FROM courses WHERE course_id = ?;";

	private JdbcTemplate jdbcTemplate;
	private LectureDao lectureDao;

	public CourseDaoImpl() {
		// NOP
	}
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public void setLectureDao(LectureDao lectureDao) {
		this.lectureDao = lectureDao;
	}

	@Override
	public List<Course> getAll() {
		return jdbcTemplate.query(GET_ALL_SQL, 
				(rs, rowNum) -> {
					int courseId = rs.getInt(COURSE_ID);
					String courseName = rs.getString(COURSE_NAME);
					List<Lecture> courseLectures = lectureDao.getByCourseId(courseId);
					return new Course(courseId, courseName, courseLectures);
		});
	}
	
	@Override
	public List<Course> getAllByParticipantId(int participantId) {
		return jdbcTemplate.query(
				GET_ALL_BY_PARTICIPANT_ID_SQL, 
				ps -> ps.setInt(1, participantId),
				(rs, rowNum) -> {
					int courseId = rs.getInt(COURSE_ID);
					String courseName = rs.getString(COURSE_NAME);
					List<Lecture> courseLectures = lectureDao.getByCourseId(courseId);
					return new Course(courseId, courseName, courseLectures);
				}
		);
	}

	@Override
	public Optional<Course> getById(int id) {
		return jdbcTemplate.query(con -> con.prepareStatement(GET_BY_ID_SQL), 
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

	@Override
	public void add(Course course) {
		int courseId = jdbcTemplate.queryForObject(ADD_SQL, Long.class, course.getName()).intValue();
		course.setId(courseId);
		
		List<Lecture> lectures = course.getLectures();
		if(!lectures.isEmpty()) {
			setCourseIdToLectures(courseId, lectures);
			lectureDao.addAll(lectures);
		}
	}

	@Override
	public void removeById(int id) {
		jdbcTemplate.update(REMOVE_BY_ID_SQL, id);
	}
	
	private void setCourseIdToLectures(int courseId, List<Lecture> lectures) {
		lectures.forEach(lecture -> lecture.setCourseId(courseId));
	}
	
}
