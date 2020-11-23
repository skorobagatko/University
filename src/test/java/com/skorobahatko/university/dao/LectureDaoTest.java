package com.skorobahatko.university.dao;

import static com.skorobahatko.university.dao.util.DaoTestUtils.*;
import static com.skorobahatko.university.dao.util.DatabaseTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.skorobahatko.university.domain.Course;
import com.skorobahatko.university.domain.Lecture;

class LectureDaoTest {
	
	private LectureDao lectureDao;

	@BeforeEach
	void setUp() throws Exception {
		initializeTestDatabase();
		
		lectureDao = new LectureDao(getTestDataSource());
	}

	@Test
	void testGetByCourseId() throws SQLException {
		Course course = getTestCourse();
		int courseId = course.getId();
		insertCourseToTheTestDatabase(course);
		
		List<Lecture> expected = getTestLecturesWith(courseId);
		insertLecturesToTheTestDatabase(expected);
		
		List<Lecture> actual = lectureDao.getByCourseId(courseId);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetByCourseIdForNonExistCourseId() throws SQLException {
		List<Lecture> expected = List.of();
		
		List<Lecture> actual = lectureDao.getByCourseId(Integer.MIN_VALUE);
		
		assertEquals(expected, actual);
	}

	@Test
	void testAddAll() throws SQLException {
		Course course = getTestCourse();
		int courseId = course.getId();
		insertCourseToTheTestDatabase(course);
		
		List<Lecture> lectures = getTestLecturesWith(courseId);
		insertLecturesToTheTestDatabase(lectures);
		
		int expected = lectures.size();
		
		int actual = lectureDao.getByCourseId(courseId).size();
		
		assertEquals(expected, actual);
	}

	@Test
	void testAdd() throws SQLException {
		Course course = getTestCourse();
		int courseId = course.getId();
		insertCourseToTheTestDatabase(course);
		
		Lecture lecture = new Lecture(1, "Lecture 1", courseId, LocalDate.of(2020, 12, 1), LocalTime.of(7, 30), LocalTime.of(9, 00), 100);
		
		List<Lecture> expected = List.of(lecture);
		insertLecturesToTheTestDatabase(expected);
		
		List<Lecture> actual = lectureDao.getByCourseId(courseId);
		
		assertEquals(expected, actual);
	}

	@Test
	void testRemoveById() throws SQLException {
		Course course = getTestCourse();
		int courseId = course.getId();
		insertCourseToTheTestDatabase(course);
		
		int lectureId = 1;
		Lecture lecture = new Lecture(lectureId, "Lecture 1", courseId, LocalDate.of(2020, 12, 1), LocalTime.of(7, 30), LocalTime.of(9, 00), 100);
		insertLecturesToTheTestDatabase(List.of(lecture));
		
		List<Lecture> expected = List.of();
		
		lectureDao.removeById(lectureId);
		
		List<Lecture> actual = lectureDao.getByCourseId(courseId);
		
		assertEquals(expected, actual);
	}

}
