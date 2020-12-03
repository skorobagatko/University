package com.skorobahatko.university.service;

import static com.skorobahatko.university.util.TestUtils.*;
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

import com.skorobahatko.university.dao.CourseDao;
import com.skorobahatko.university.domain.Course;
import com.skorobahatko.university.domain.Lecture;

@SqlGroup({ 
	@Sql("/delete_tables.sql"), 
	@Sql("/create_tables.sql"), 
	@Sql("/populate_courses.sql"),
	@Sql("/populate_lectures.sql") 
})
@Sql(scripts = "/delete_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@ContextConfiguration("/applicationContext.xml")
class LectureServiceImplIT {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private CourseDao courseDao;
	
	@Autowired
	private LectureService lectureService;

	@Test
	void testGetAll() throws SQLException {
		int expected = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lectures");
		int actual = lectureService.getAll().size();

		assertEquals(expected, actual);
	}

	@Test
	void testGetById() throws SQLException {
		Course course = getTestCourse();
		courseDao.add(course);
		Lecture lecture = getTestLectureWithCourseId(course.getId());
		lectureService.add(lecture);
		
		int expectedId = lecture.getId();
		int actualId = lectureService.getById(expectedId).get().getId();

		assertEquals(expectedId, actualId);
	}
	
	@Test
	void testGetByIdForNonExistLectureId() throws SQLException {
		Optional<Lecture> expected = Optional.empty();
		Optional<Lecture> actual = lectureService.getById(Integer.MIN_VALUE);

		assertEquals(expected, actual);
	}

	@Test
	void testGetByCourseId() throws SQLException {
		int expectedCourseId = 1;

		List<Lecture> lectures = lectureService.getByCourseId(expectedCourseId);

		for (Lecture lecture : lectures) {
			assertEquals(expectedCourseId, lecture.getCourseId());
		}
	}

	@Test
	void testGetByCourseIdForNonExistCourseId() throws SQLException {
		List<Lecture> expected = List.of();
		List<Lecture> actual = lectureService.getByCourseId(Integer.MIN_VALUE);

		assertEquals(expected, actual);
	}

	@Test
	void testAddAll() throws SQLException {
		int rowsCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lectures");

		Course course = getTestCourse();
		courseDao.add(course);
		List<Lecture> lectures = getTestLecturesWithCourseId(course.getId());

		lectureService.addAll(lectures);

		int expected = rowsCount + lectures.size();
		int actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lectures");

		assertEquals(expected, actual);
	}

	@Test
	void testAdd() throws SQLException {
		int rowsCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lectures");
		
		Course course = getTestCourse();
		courseDao.add(course);
		Lecture lecture = getTestLectureWithCourseId(course.getId());
		
		lectureService.add(lecture);
		
		int expected = rowsCount + 1;
		int actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lectures");
		
		assertEquals(expected, actual);
	}

	@Test
	void testRemoveById() throws SQLException {
		Course course = getTestCourse();
		courseDao.add(course);
		Lecture lecture = getTestLectureWithCourseId(course.getId());
		lectureService.add(lecture);
		int lectureId = lecture.getId();
		
		lectureService.removeById(lectureId);
		
		Optional<Lecture> expected = Optional.empty();
		Optional<Lecture> actual = lectureService.getById(lectureId);
		
		assertEquals(expected, actual);
	}

}
