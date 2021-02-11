package com.skorobahatko.university.dao;

import static com.skorobahatko.university.util.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import com.skorobahatko.university.dao.exception.EntityNotFoundDaoException;
import com.skorobahatko.university.domain.Course;
import com.skorobahatko.university.domain.Lecture;

@SqlGroup({ 
	@Sql("/delete_tables.sql"), 
	@Sql("/create_tables.sql"), 
	@Sql("/populate_courses.sql"),
	@Sql("/populate_lectures.sql") 
})
@Sql(scripts = "/delete_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class LectureDaoImplIT {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private LectureDao lectureDao;

	@Autowired
	private CourseDao courseDao;

	@Test
	void testGetAll() throws SQLException {
		int expected = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lectures");

		int actual = lectureDao.getAll().size();

		assertEquals(expected, actual);
	}

	@Test
	void testGetById() throws SQLException {
		Course course = getTestCourse();
		courseDao.add(course);
		Lecture lecture = new Lecture("Test Lecture", course.getId(), LocalDate.of(2020, 12, 1), 
				LocalTime.of(7, 30), LocalTime.of(9, 00), 100);
		lectureDao.add(lecture);
		
		int expected = lecture.getId();

		int actual = lectureDao.getById(expected).getId();

		assertEquals(expected, actual);
	}

	@Test
	void testGetByCourseId() throws SQLException {
		int expectedCourseId = 1;

		List<Lecture> lectures = lectureDao.getByCourseId(expectedCourseId);

		for (Lecture lecture : lectures) {
			assertEquals(expectedCourseId, lecture.getCourseId());
		}
	}

	@Test
	void testGetByCourseIdForNonExistCourseId() throws SQLException {
		List<Lecture> expected = List.of();

		List<Lecture> actual = lectureDao.getByCourseId(Integer.MIN_VALUE);

		assertEquals(expected, actual);
	}

	@Test
	void testAddAll() throws SQLException {
		int rowsCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lectures");

		Course course = getTestCourse();
		courseDao.add(course);
		List<Lecture> lectures = getTestLecturesWithCourseId(course.getId());

		lectureDao.addAll(lectures);

		int expected = rowsCount + lectures.size();

		int actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lectures");

		assertEquals(expected, actual);
	}

	@Test
	void testAdd() throws SQLException {
		int rowsCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lectures");
		
		Course course = getTestCourse();
		courseDao.add(course);
		Lecture lecture = new Lecture("Test Lecture", course.getId(), LocalDate.of(2020, 12, 1), 
				LocalTime.of(7, 30), LocalTime.of(9, 00), 100);
		
		lectureDao.add(lecture);
		
		int expected = rowsCount + 1;
		
		int actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lectures");
		
		assertEquals(expected, actual);
	}

	@Test
	void testRemoveById() throws SQLException {
		Course course = getTestCourse();
		courseDao.add(course);
		Lecture lecture = new Lecture("Test Lecture", course.getId(), LocalDate.of(2020, 12, 1), 
				LocalTime.of(7, 30), LocalTime.of(9, 00), 100);
		lectureDao.add(lecture);
		int lectureId = lecture.getId();
		
		lectureDao.removeById(lectureId);
		
		assertThrows(EntityNotFoundDaoException.class, () -> lectureDao.getById(lectureId));
	}

}
