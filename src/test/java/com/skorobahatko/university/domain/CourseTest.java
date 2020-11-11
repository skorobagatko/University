package com.skorobahatko.university.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CourseTest {
	
	private Course course;

	@BeforeEach
	void setUp() throws Exception {
		course = getTestCourse();
	}

	@Test
	void testAddLecture() {
		String courseId = "TST-100";
		String courseName = "Test course";
		course = new Course(courseId, courseName);
		Lecture lecture = getTestLecture();
		
		course.addLecture(lecture);
		
		assertTrue(course.getLectures().contains(lecture));
	}

	@Test
	void testRemoveLecture() {
		Lecture lecture = getTestLecture();
		
		course.removeLecture(lecture);
		
		assertTrue(course.getLectures().isEmpty());
	}
	
	@Test
	void testEqualsMethodReturnsTrueForEqualCourses() {
		Course other = getTestCourse();
		
		assertTrue(course.equals(other));
	}
	
	@Test
	void testEqualsMethodReturnsFalseForNonEqualCourses() {
		String courseId = "QWERTY-999";
		String courseName = "Qwerty course";
		Course other = new Course(courseId, courseName);
		
		assertTrue(!course.equals(other));
	}
	
	@Test
	void testEqualsMethodReturnsTrueForSameInstanceCourse() {
		Course other = course;
		
		assertTrue(course.equals(other));
	}
	
	@Test
	void testEqualsMethodReturnsFalseForNullArgument() {
		Course other = null;
		
		assertTrue(!course.equals(other));
	}
	
	@Test
	void testEqualsMethodReturnsFalseWithObjectArgument() {
		Object other = new Object();
		
		assertTrue(!course.equals(other));
	}
	
	private Course getTestCourse() {
		String courseId = "TST-100";
		String courseName = "Test course";
		course = new Course(courseId, courseName);
		
		Lecture lecture = getTestLecture();
		course.addLecture(lecture);
		
		return course;
	}
	
	private Lecture getTestLecture() {
		String lectureName = "Recursive Algorithms";
		LocalDate lectureDate = LocalDate.of(2020, 11, 5);
		LocalTime lectureStartTime = LocalTime.of(7, 30);
		LocalTime lectureEndTime = LocalTime.of(9, 0);
		int roomNumber = 201;
		Lecture lecture = new Lecture(lectureName, course, lectureDate, lectureStartTime, lectureEndTime, roomNumber);
		
		return lecture;
	}

}
