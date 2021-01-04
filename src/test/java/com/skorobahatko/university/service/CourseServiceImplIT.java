package com.skorobahatko.university.service;

import static com.skorobahatko.university.util.TestUtils.getTestCourse;
import static com.skorobahatko.university.util.TestUtils.getTestParticipant;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.List;

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
import com.skorobahatko.university.service.exception.EntityNotFoundServiceException;
import com.skorobahatko.university.service.exception.ValidationException;

@SqlGroup({ 
	@Sql("/delete_tables.sql"), 
	@Sql("/create_tables.sql"), 
	@Sql("/populate_courses.sql"),
	@Sql("/populate_participants.sql")
})
@Sql(scripts = "/delete_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/applicationContext.xml")
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
		int actual = courseService.getById(expected).getId();

		assertEquals(expected, actual);
	}
	
	@Test
	void testGetByIdThrowsExceptionForNonExistCourse() {
		int courseId = Integer.MAX_VALUE;

		assertThrows(EntityNotFoundServiceException.class, () -> courseService.getById(courseId));
	}
	
	@Test
	void testGetByIdThrowsExceptionForNonValidCourseId() {
		int courseId = -1;

		assertThrows(ValidationException.class, () -> courseService.getById(courseId));
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
	void testAddThrowsExceptionForNullCourseArgument() {
		Course course = null;
		
		assertThrows(ValidationException.class, () -> courseService.add(course));
	}

	@Test
	void testRemoveById() throws SQLException {
		Course course = getTestCourse();
		courseService.add(course);
		int courseId = course.getId();
		
		courseService.removeById(courseId);
		
		assertThrows(EntityNotFoundServiceException.class, () -> courseService.getById(courseId));
	}

}
