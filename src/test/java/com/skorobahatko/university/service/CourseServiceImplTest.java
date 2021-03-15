package com.skorobahatko.university.service;

import static com.skorobahatko.university.util.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.skorobahatko.university.domain.Course;
import com.skorobahatko.university.repository.CourseRepository;
import com.skorobahatko.university.service.exception.EntityNotFoundException;
import com.skorobahatko.university.service.exception.ValidationException;

@RunWith(MockitoJUnitRunner.class)
class CourseServiceImplTest {
	
	private CourseRepository courseRepository;
	private CourseService courseService;
	
	@BeforeEach
    public void init() {
		courseRepository = Mockito.mock(CourseRepository.class);
		courseService = new CourseServiceImpl(courseRepository);
	}

	@Test
	void testGetAll() {
		Course course = getTestCourse();
		List<Course> expected = List.of(course);
		
		Mockito.when(courseRepository.findAll()).thenReturn(expected);
		
		List<Course> actual = courseService.getAll();
		
		assertEquals(expected, actual);
	}

	@Test
	void testGetById() {
		Course expected = getTestCourse();
		int courseId = 1;
		expected.setId(courseId);
		
		Mockito.when(courseRepository.findById(courseId)).thenReturn(Optional.of(expected));
		
		Course actual = courseService.getById(courseId);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetByIdThrowsExceptionForNonExistCourse() {
		int courseId = Integer.MAX_VALUE;
		
		Mockito.when(courseRepository.findById(courseId)).thenThrow(EntityNotFoundException.class);
		
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
		
		Mockito.verify(courseRepository).save(course);
	}
	
	@Test
	void testAddThrowsExceptionForNullCourseArgument() {
		Course course = null;
		
		assertThrows(ValidationException.class, () -> courseService.add(course));
	}

	@Test
	void testRemoveById() {
		courseService.removeById(1);
		
		Mockito.verify(courseRepository).deleteById(1);
	}

}
