package com.skorobahatko.university.service;

import static com.skorobahatko.university.util.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.skorobahatko.university.dao.LectureDao;
import com.skorobahatko.university.domain.Lecture;
import com.skorobahatko.university.service.exception.EntityNotFoundServiceException;
import com.skorobahatko.university.service.exception.ValidationException;

class LectureServiceImplTest {
	
	private LectureServiceImpl lectureService;

	@BeforeEach
	void setUp() throws Exception {
		lectureService = new LectureServiceImpl();
	}

	@Test
	void testGetAll() {
		List<Lecture> expected = getTestLecturesWithCourseId(1);
		
		LectureDao lectureDao = Mockito.mock(LectureDao.class);
		Mockito.when(lectureDao.getAll()).thenReturn(expected);
		lectureService.setLectureDao(lectureDao);
		
		List<Lecture> actual = lectureService.getAll();
		
		assertEquals(expected, actual);
	}

	@Test
	void testGetById() {
		Lecture expected = getTestLectureWithCourseId(1);
		int lectureId = 3;
		expected.setId(lectureId);
		
		LectureDao lectureDao = Mockito.mock(LectureDao.class);
		Mockito.when(lectureDao.getById(lectureId)).thenReturn(expected);
		lectureService.setLectureDao(lectureDao);
		
		Lecture actual = lectureService.getById(lectureId);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetByIdThrowsExceptionForNonExistCourse() {
		int lectureId = Integer.MAX_VALUE;
		
		LectureDao lectureDao = Mockito.mock(LectureDao.class);
		Mockito.when(lectureDao.getById(lectureId)).thenThrow(EntityNotFoundServiceException.class);
		lectureService.setLectureDao(lectureDao);
		
		assertThrows(EntityNotFoundServiceException.class, () -> lectureService.getById(lectureId));
	}
	
	@Test
	void testGetByIdThrowsExceptionForNonValidCourseId() {
		int lectureId = -1;

		assertThrows(ValidationException.class, () -> lectureService.getById(lectureId));
	}

	@Test
	void testGetByCourseId() {
		int courseId = 7;
		Lecture lecture = getTestLectureWithCourseId(courseId);
		
		List<Lecture> expected = List.of(lecture);
		
		LectureDao lectureDao = Mockito.mock(LectureDao.class);
		Mockito.when(lectureDao.getByCourseId(courseId)).thenReturn(expected);
		lectureService.setLectureDao(lectureDao);
		
		List<Lecture> actual = lectureService.getByCourseId(courseId);
		
		assertEquals(expected, actual);
	}

	@Test
	void testAddAll() {
		List<Lecture> lectures = getTestLecturesWithCourseId(1);
		
		LectureDao lectureDao = Mockito.mock(LectureDao.class);
		lectureService.setLectureDao(lectureDao);
		
		lectureService.addAll(lectures);
		
		Mockito.verify(lectureDao).addAll(lectures);
	}

	@Test
	void testAdd() {
		Lecture lecture = getTestLectureWithCourseId(1);
		
		LectureDao lectureDao = Mockito.mock(LectureDao.class);
		lectureService.setLectureDao(lectureDao);
		
		lectureService.add(lecture);
		
		Mockito.verify(lectureDao).add(lecture);
	}
	
	@Test
	void testAddThrowsExceptionForNullLectureArgument() {
		Lecture lecture = null;
		
		assertThrows(ValidationException.class, () -> lectureService.add(lecture));
	}

	@Test
	void testRemoveById() {
		LectureDao lectureDao = Mockito.mock(LectureDao.class);
		lectureService.setLectureDao(lectureDao);
		
		lectureService.removeById(1);
		
		Mockito.verify(lectureDao).removeById(1);
	}

}
