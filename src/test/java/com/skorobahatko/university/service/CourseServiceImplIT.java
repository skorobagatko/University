package com.skorobahatko.university.service;

import static com.skorobahatko.university.util.TestUtils.getTestCourse;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;

import com.skorobahatko.university.domain.Course;
import com.skorobahatko.university.service.exception.EntityNotFoundException;
import com.skorobahatko.university.service.exception.ValidationException;

@SqlGroup({ 
	@Sql("/delete_tables.sql"), 
	@Sql("/create_tables.sql"), 
	@Sql("/populate_courses.sql"),
	@Sql("/populate_participants.sql")
})
@Sql(scripts = "/delete_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

@RunWith(SpringRunner.class)
@SpringBootTest
class CourseServiceImplIT {
	
	@Autowired
	private CourseService courseService;

	@Test
	void testGetAll() throws SQLException {
		int expected = 3;
		int actual = courseService.getAll().size();

		assertEquals(expected, actual);
	}

	@Test
	void testGetById() throws SQLException {
		Course course = getTestCourse();
		course = courseService.add(course);
		
		int expected = course.getId();
		int actual = courseService.getById(expected).getId();

		assertEquals(expected, actual);
	}
	
	@Test
	void testGetByIdThrowsExceptionForNonExistCourse() {
		int courseId = Integer.MAX_VALUE;

		assertThrows(EntityNotFoundException.class, () -> courseService.getById(courseId));
	}
	
	@Test
	void testGetByIdThrowsExceptionForNonValidCourseId() {
		int courseId = -1;

		assertThrows(ValidationException.class, () -> courseService.getById(courseId));
	}

	@Test
	void testAdd() {
		Course course = getTestCourse();
		
		courseService.add(course);
		
		int expected = 4;
		int actual = courseService.getAll().size();
		
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
		course = courseService.add(course);
		int courseId = course.getId();
		
		courseService.removeById(courseId);
		
		assertThrows(EntityNotFoundException.class, () -> courseService.getById(courseId));
	}

}
