package com.skorobahatko.university.service;

import static com.skorobahatko.university.util.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;

import com.skorobahatko.university.domain.Course;
import com.skorobahatko.university.domain.Lecture;
import com.skorobahatko.university.service.exception.EntityNotFoundException;
import com.skorobahatko.university.service.exception.ValidationException;

@SqlGroup({ 
	@Sql("/delete_tables.sql"), 
	@Sql("/create_tables.sql"), 
	@Sql("/populate_courses.sql"),
	@Sql("/populate_lectures.sql") 
})
@Sql(scripts = "/delete_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

@RunWith(SpringRunner.class)
@SpringBootTest
class LectureServiceImplIT {

	@Autowired
	private CourseService courseService;
	
	@Autowired
	private LectureService lectureService;

	@Test
	void testGetAll() {
		int expected = 6;
		int actual = lectureService.getAll().size();

		assertEquals(expected, actual);
	}

	@Test
	void testGetById() {
		Course course = getTestCourse();
		course = courseService.add(course);
		Lecture lecture = getTestLectureWithCourseId(course.getId());
		lecture = lectureService.add(lecture);
		
		int expectedId = lecture.getId();
		int actualId = lectureService.getById(expectedId).getId();

		assertEquals(expectedId, actualId);
	}
	
	@Test
	void testGetByIdThrowsExceptionForNonExistLecture() {
		int lectureId = Integer.MAX_VALUE;

		assertThrows(EntityNotFoundException.class, () -> lectureService.getById(lectureId));
	}
	
	@Test
	void testGetByIdThrowsExceptionForNonValidLectureId() {
		int lectureId = -1;

		assertThrows(ValidationException.class, () -> lectureService.getById(lectureId));
	}

	@Test
	void testGetByCourseId() {
		int expectedCourseId = 1;

		List<Lecture> lectures = lectureService.getByCourseId(expectedCourseId);

		for (Lecture lecture : lectures) {
			assertEquals(expectedCourseId, lecture.getCourseId());
		}
	}

	@Test
	void testGetByCourseIdForNonExistCourseId() {
		List<Lecture> expected = List.of();
		List<Lecture> actual = lectureService.getByCourseId(Integer.MAX_VALUE);

		assertEquals(expected, actual);
	}

	@Test
	void testAddAll() {
		Course course = getTestCourse();
		course = courseService.add(course);
		List<Lecture> lectures = getTestLecturesWithCourseId(course.getId());

		lectureService.addAll(lectures);

		int expected = 6 + lectures.size();
		int actual = lectureService.getAll().size();

		assertEquals(expected, actual);
	}

	@Test
	void testAdd() {
		Course course = getTestCourse();
		course = courseService.add(course);
		Lecture lecture = getTestLectureWithCourseId(course.getId());
		
		lecture = lectureService.add(lecture);
		
		int expected = 7;
		int actual = lectureService.getAll().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testAddThrowsExceptionForNullLectureArgument() {
		Lecture lecture = null;
		
		assertThrows(ValidationException.class, () -> lectureService.add(lecture));
	}

	@Test
	void testRemoveById() {
		Course course = getTestCourse();
		course = courseService.add(course);
		Lecture lecture = getTestLectureWithCourseId(course.getId());
		lecture = lectureService.add(lecture);
		int lectureId = lecture.getId();
		
		lectureService.removeById(lectureId);
		
		assertThrows(EntityNotFoundException.class, () -> lectureService.getById(lectureId));
	}

}
