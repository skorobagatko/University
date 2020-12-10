package com.skorobahatko.university.service;

import static com.skorobahatko.university.util.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.skorobahatko.university.dao.CourseDao;
import com.skorobahatko.university.domain.Course;
import com.skorobahatko.university.service.exception.EntityNotFoundServiceException;
import com.skorobahatko.university.service.exception.ValidationException;

class CourseServiceImplTest {
	
	private CourseServiceImpl courseService;

	@BeforeEach
	void setUp() throws Exception {
		courseService = new CourseServiceImpl();
	}

	@Test
	void testGetAll() {
		Course course = getTestCourse();
		List<Course> expected = List.of(course);
		
		CourseDao courseDao = Mockito.mock(CourseDao.class);
		Mockito.when(courseDao.getAll()).thenReturn(expected);
		courseService.setCourseDao(courseDao);
		
		List<Course> actual = courseService.getAll();
		
		assertEquals(expected, actual);
	}

	@Test
	void testGetAllByParticipantId() {
		Course course = getTestCourse();
		List<Course> expected = List.of(course);
		
		CourseDao courseDao = Mockito.mock(CourseDao.class);
		Mockito.when(courseDao.getAllByParticipantId(1)).thenReturn(expected);
		courseService.setCourseDao(courseDao);
		
		List<Course> actual = courseService.getAllByParticipantId(1);
		
		assertEquals(expected, actual);
	}

	@Test
	void testGetById() {
		Course expected = getTestCourse();
		int courseId = 1;
		expected.setId(courseId);
		
		CourseDao courseDao = Mockito.mock(CourseDao.class);
		Mockito.when(courseDao.getById(courseId)).thenReturn(expected);
		courseService.setCourseDao(courseDao);
		
		Course actual = courseService.getById(courseId);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetByIdThrowsExceptionForNonExistCourse() {
		int courseId = Integer.MAX_VALUE;
		
		CourseDao courseDao = Mockito.mock(CourseDao.class);
		Mockito.when(courseDao.getById(courseId)).thenThrow(EntityNotFoundServiceException.class);
		courseService.setCourseDao(courseDao);
		
		assertThrows(EntityNotFoundServiceException.class, () -> courseService.getById(courseId));
	}
	
	@Test
	void testGetByIdThrowsExceptionForNonValidCourseId() {
		int courseId = -1;

		assertThrows(ValidationException.class, () -> courseService.getById(courseId));
	}

	@Test
	void testAdd() {
		Course course = getTestCourse();
		
		CourseDao courseDao = Mockito.mock(CourseDao.class);
		courseService.setCourseDao(courseDao);
		
		courseService.add(course);
		
		Mockito.verify(courseDao).add(course);
	}
	
	@Test
	void testAddThrowsExceptionForNullCourseArgument() {
		Course course = null;
		
		assertThrows(ValidationException.class, () -> courseService.add(course));
	}

	@Test
	void testRemoveById() {
		CourseDao courseDao = Mockito.mock(CourseDao.class);
		courseService.setCourseDao(courseDao);
		
		courseService.removeById(1);
		
		Mockito.verify(courseDao).removeById(1);
	}

}
