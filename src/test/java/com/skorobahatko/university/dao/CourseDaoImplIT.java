package com.skorobahatko.university.dao;

import static com.skorobahatko.university.util.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;

import com.skorobahatko.university.dao.exception.EntityNotFoundDaoException;
import com.skorobahatko.university.domain.Course;

@SqlGroup({ 
	@Sql("/delete_tables.sql"), 
	@Sql("/create_tables.sql"), 
	@Sql("/populate_courses.sql"),
	@Sql("/populate_participants.sql")
})
@Sql(scripts = "/delete_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@ContextConfiguration("file:src/test/resources/springTestContext.xml")
class CourseDaoImplIT {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private CourseDao courseDao;

	@Test
	void testGetAll() throws SQLException {
		int expected = JdbcTestUtils.countRowsInTable(jdbcTemplate, "courses");
		int actual = courseDao.getAll().size();

		assertEquals(expected, actual);
	}

	@Test
	void testGetById() throws SQLException {
		Course course = getTestCourse();
		courseDao.add(course);
		
		int expected = course.getId();
		int actual = courseDao.getById(expected).getId();

		assertEquals(expected, actual);
	}
	
	@Test
	void testGetByIdThrowsExceptionForNonExistCourse() throws SQLException {
		int courseId = Integer.MIN_VALUE;

		assertThrows(EntityNotFoundDaoException.class, () -> courseDao.getById(courseId));
	}

	@Test
	void testAdd() {
		int rowsCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "courses");
		
		Course course = getTestCourse();
		
		courseDao.add(course);
		
		int expected = rowsCount + 1;
		int actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, "courses");
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testUpdate() {
		Course course = getTestCourse();
		courseDao.add(course);
		int courseId = course.getId();
		
		String newCourseName = "New Course Name";
		course.setName(newCourseName);
		
		courseDao.update(course);
		
		String actualCourseName = courseDao.getById(courseId).getName();
		
		assertEquals(actualCourseName, newCourseName);
	}

	@Test
	void testRemoveById() throws SQLException {
		Course course = getTestCourse();
		courseDao.add(course);
		int courseId = course.getId();
		
		courseDao.removeById(courseId);
		
		assertThrows(EntityNotFoundDaoException.class, () -> courseDao.getById(courseId));
	}

}
