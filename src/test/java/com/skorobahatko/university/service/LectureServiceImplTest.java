package com.skorobahatko.university.service;

import static com.skorobahatko.university.util.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.skorobahatko.university.dao.LectureDao;
import com.skorobahatko.university.domain.Lecture;

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
		Lecture lecture = getTestLectureWithCourseId(1);
		int lectureId = 3;
		lecture.setId(lectureId);
		Optional<Lecture> expected = Optional.of(lecture);
		
		LectureDao lectureDao = Mockito.mock(LectureDao.class);
		Mockito.when(lectureDao.getById(lectureId)).thenReturn(expected);
		lectureService.setLectureDao(lectureDao);
		
		Optional<Lecture> actual = lectureService.getById(lectureId);
		
		assertEquals(expected, actual);
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
	void testRemoveById() {
		LectureDao lectureDao = Mockito.mock(LectureDao.class);
		lectureService.setLectureDao(lectureDao);
		
		lectureService.removeById(1);
		
		Mockito.verify(lectureDao).removeById(1);
	}

}
