package com.skorobahatko.university.dao;

import static org.junit.jupiter.api.Assertions.*;
import static com.skorobahatko.university.dao.util.DatabaseTestUtils.*;
import static com.skorobahatko.university.dao.util.DaoTestUtils.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.skorobahatko.university.domain.Course;
import com.skorobahatko.university.domain.Lecture;
import com.skorobahatko.university.domain.Participant;

class CourseDaoTest {
	
	private CourseDao courseDao;

	@BeforeEach
	void setUp() throws Exception {
		initializeTestDatabase();
		
		courseDao = new CourseDao(getTestDataSource());
	}

	@Test
	void testGetAll() throws SQLException {
		Course course = getTestCourse();
		insertCourseToTheTestDatabase(course);
		
		List<Course> expected = List.of(course);
		
		List<Course> actual = courseDao.getAll();
		
		assertEquals(expected, actual);
	}

	@Test
	void testGetAllByParticipantId() throws SQLException {
		Course course = getTestCourse();
		int courseId = course.getId();
		List<Lecture> lectures = getTestLecturesWith(courseId);
		course.setLectures(lectures);
		
		Participant participant = getTestParticipant();
		participant.addCourse(course);
		
		insertCourseToTheTestDatabase(course);
		insertLecturesToTheTestDatabase(lectures);
		insertParticipantToTheTestDatabase(participant);
		
		List<Course> expected = List.of(course);
		
		List<Course> actual = courseDao.getAllByParticipantId(participant.getId());
		
		assertEquals(expected, actual);
	}

	@Test
	void testGetById() throws SQLException {
		Course course = getTestCourse();
		int courseId = course.getId();
		insertCourseToTheTestDatabase(course);
		
		Optional<Course> expected = Optional.of(course);
		
		Optional<Course> actual = courseDao.getById(courseId);

		assertEquals(expected, actual);
	}
	
	@Test
	void testGetByIdForNonExistCourse() throws SQLException {
		int courseId = Integer.MIN_VALUE;
		
		Optional<Course> expected = Optional.empty();
		
		Optional<Course> actual = courseDao.getById(courseId);

		assertEquals(expected, actual);
	}

	@Test
	void testAdd() {
		Course course = getTestCourse();
		int courseId = course.getId();
		courseDao.add(course);
		
		Optional<Course> expected = Optional.of(course);
		
		Optional<Course> actual = courseDao.getById(courseId);
		
		assertEquals(expected, actual);
	}

	@Test
	void testRemoveById() throws SQLException {
		Course course = getTestCourse();
		int courseId = course.getId();
		insertCourseToTheTestDatabase(course);
		
		courseDao.removeById(courseId);
		
		Optional<Course> expected = Optional.empty();
		
		Optional<Course> actual = courseDao.getById(courseId);
		
		assertEquals(expected, actual);
	}

}
