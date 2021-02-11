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

import com.skorobahatko.university.dao.LectureDao;
import com.skorobahatko.university.domain.Lecture;
import com.skorobahatko.university.service.exception.EntityNotFoundServiceException;
import com.skorobahatko.university.service.exception.ValidationException;

@RunWith(SpringRunner.class)
@SpringBootTest
class LectureServiceImplTest {
	
	@MockBean
	private LectureDao lectureDao;
	
	@Autowired
	private LectureService lectureService;

	@Test
	void testGetAll() {
		List<Lecture> expected = getTestLecturesWithCourseId(1);
		
		Mockito.when(lectureDao.getAll()).thenReturn(expected);
		
		List<Lecture> actual = lectureService.getAll();
		
		assertEquals(expected, actual);
	}

	@Test
	void testGetById() {
		Lecture expected = getTestLectureWithCourseId(1);
		int lectureId = 3;
		expected.setId(lectureId);
		
		Mockito.when(lectureDao.getById(lectureId)).thenReturn(expected);
		
		Lecture actual = lectureService.getById(lectureId);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetByIdThrowsExceptionForNonExistCourse() {
		int lectureId = Integer.MAX_VALUE;
		
		Mockito.when(lectureDao.getById(lectureId)).thenThrow(EntityNotFoundServiceException.class);
		
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
		
		Mockito.when(lectureDao.getByCourseId(courseId)).thenReturn(expected);
		
		List<Lecture> actual = lectureService.getByCourseId(courseId);
		
		assertEquals(expected, actual);
	}

	@Test
	void testAddAll() {
		List<Lecture> lectures = getTestLecturesWithCourseId(1);
		
		lectureService.addAll(lectures);
		
		Mockito.verify(lectureDao).addAll(lectures);
	}

	@Test
	void testAdd() {
		Lecture lecture = getTestLectureWithCourseId(1);
		
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
		lectureService.removeById(1);
		
		Mockito.verify(lectureDao).removeById(1);
	}

}
