package com.skorobahatko.university.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;

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
		int courseId = 1;
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
		int courseId = 1234567;
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
		int courseId = 1;
		String courseName = "Test course";
		course = new Course(courseId, courseName);
		
		Lecture lecture = getTestLecture();
		course.addLecture(lecture);
		
		return course;
	}
	
	private Lecture getTestLecture() {
		int id = 1;
		String lectureName = "Test lecture";
		LocalDate lectureDate = LocalDate.of(2020, 11, 5);
		LocalTime lectureStartTime = LocalTime.of(7, 30);
		LocalTime lectureEndTime = LocalTime.of(9, 0);
		int roomNumber = 201;
		Lecture lecture = new Lecture(id, lectureName, course.getId(), lectureDate, lectureStartTime, lectureEndTime, roomNumber);
		
		return lecture;
	}

}
