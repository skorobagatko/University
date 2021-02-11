package com.skorobahatko.university.service;

import static com.skorobahatko.university.util.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.skorobahatko.university.dao.CourseDao;
import com.skorobahatko.university.domain.Course;
import com.skorobahatko.university.service.exception.EntityNotFoundServiceException;
import com.skorobahatko.university.service.exception.ValidationException;

@RunWith(SpringRunner.class)
@SpringBootTest
class CourseServiceImplTest {
	
	@MockBean
	private CourseDao courseDao;
	
	@Autowired
	private CourseService courseService;

	@Test
	void testGetAll() {
		Course course = getTestCourse();
		List<Course> expected = List.of(course);
		
		Mockito.when(courseDao.getAll()).thenReturn(expected);
		
		List<Course> actual = courseService.getAll();
		
		assertEquals(expected, actual);
	}

	@Test
	void testGetById() {
		Course expected = getTestCourse();
		int courseId = 1;
		expected.setId(courseId);
		
		Mockito.when(courseDao.getById(courseId)).thenReturn(expected);
		
		Course actual = courseService.getById(courseId);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetByIdThrowsExceptionForNonExistCourse() {
		int courseId = Integer.MAX_VALUE;
		
		Mockito.when(courseDao.getById(courseId)).thenThrow(EntityNotFoundServiceException.class);
		
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
		courseService.removeById(1);
		
		Mockito.verify(courseDao).removeById(1);
	}

}
