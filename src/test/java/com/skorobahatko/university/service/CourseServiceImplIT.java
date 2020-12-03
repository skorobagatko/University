package com.skorobahatko.university.service;

import static com.skorobahatko.university.util.TestUtils.getTestCourse;
import static com.skorobahatko.university.util.TestUtils.getTestParticipant;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;

import com.skorobahatko.university.dao.ParticipantDao;
import com.skorobahatko.university.domain.Course;
import com.skorobahatko.university.domain.Participant;

@SqlGroup({ 
	@Sql("/delete_tables.sql"), 
	@Sql("/create_tables.sql"), 
	@Sql("/populate_courses.sql"),
	@Sql("/populate_participants.sql")
})
@Sql(scripts = "/delete_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@ContextConfiguration("/applicationContext.xml")
class CourseServiceImplIT {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ParticipantDao participantDao;
	
	@Autowired
	private CourseServiceImpl courseService;

	@Test
	void testGetAll() throws SQLException {
		int expected = JdbcTestUtils.countRowsInTable(jdbcTemplate, "courses");
		int actual = courseService.getAll().size();

		assertEquals(expected, actual);
	}

	@Test
	void testGetAllByParticipantId() throws SQLException {
		Course course = getTestCourse();
		courseService.add(course);
		
		Participant participant = getTestParticipant();
		participant.addCourse(course);
		participantDao.add(participant);
		
		List<Course> expected = List.of(course);
		List<Course> actual = courseService.getAllByParticipantId(participant.getId());
		
		assertEquals(expected, actual);
	}

	@Test
	void testGetById() throws SQLException {
		Course course = getTestCourse();
		courseService.add(course);
		
		int expected = course.getId();
		int actual = courseService.getById(expected).get().getId();

		assertEquals(expected, actual);
	}
	
	@Test
	void testGetByIdForNonExistCourse() throws SQLException {
		int courseId = Integer.MIN_VALUE;
		
		Optional<Course> expected = Optional.empty();
		Optional<Course> actual = courseService.getById(courseId);

		assertEquals(expected, actual);
	}

	@Test
	void testAdd() {
		int rowsCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "courses");
		Course course = getTestCourse();
		
		courseService.add(course);
		
		int expected = rowsCount + 1;
		int actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, "courses");
		
		assertEquals(expected, actual);
	}

	@Test
	void testRemoveById() throws SQLException {
		Course course = getTestCourse();
		courseService.add(course);
		int courseId = course.getId();
		
		courseService.removeById(courseId);
		
		Optional<Course> expected = Optional.empty();
		Optional<Course> actual = courseService.getById(courseId);
		
		assertEquals(expected, actual);
	}

}
