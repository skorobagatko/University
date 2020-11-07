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
	void testGetId() {
		String expected = "TST-100";
		
		String actual = course.getId();
		
		assertEquals(expected, actual);
	}

	@Test
	void testGetName() {
		String expected = "Test course";
		
		String actual = course.getName();
		
		assertEquals(expected, actual);
	}

	@Test
	void testGetLectures() {
		Lecture lecture = getTestLecture();
		List<Lecture> expected = List.of(lecture);
		
		List<Lecture> actual = course.getLectures();
		
		assertEquals(expected, actual);
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
	
	@Test
	void testHashcodeMethodReturnsSameHashcodeForEqualCourses() {
		course = getTestCourse();
		Course other = getTestCourse();
		
		assertEquals(course.hashCode(), other.hashCode());
	}
	
	@Test
	void testHashcodeMethodReturnsDifferentHashcodesForNonEqualCourses() {
		course = getTestCourse();
		
		String courseId = "QWERTY-123";
		String courseName = "Qwerty course";
		Course other = new Course(courseId, courseName);
		
		assertNotEquals(course.hashCode(), other.hashCode());
	}
	
	@Test
	void testToString() {
		String expected = "Course [id=TST-100, name=Test course, "
				+ "lectures=[Lecture [courseId=TST-100, name=Recursive Algorithms, "
				+ "date=2020-11-05, startTime=07:30, endTime=09:00, roomNumber=201]]]";
		
		String actual = course.toString();
		
		assertEquals(expected, actual);
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
