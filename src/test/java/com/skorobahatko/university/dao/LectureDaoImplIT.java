package com.skorobahatko.university.dao;

import static com.skorobahatko.university.dao.util.DaoTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;

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
class LectureDaoImplIT {

	@Inject
	private JdbcTemplate jdbcTemplate;

	@Inject
	private LectureDao lectureDao;

	@Inject
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

		int actual = lectureDao.getById(expected).get().getId();

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
		List<Lecture> lectures = getTestLecturesWith(course.getId());

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
		
		lectureDao.removeById(lecture.getId());
		
		Optional<Lecture> expected = Optional.empty();
		
		Optional<Lecture> actual = lectureDao.getById(lecture.getId());
		
		assertEquals(expected, actual);
	}

}
