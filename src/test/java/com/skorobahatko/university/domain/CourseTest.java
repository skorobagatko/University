package com.skorobahatko.university.domain;

import static com.skorobahatko.university.util.TestUtils.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CourseTest {
	
	private Course course;

	@BeforeEach
	void setUp() {
		course = getTestCourse();
	}

	@Test
	void testAddLecture() {
		Lecture lecture = getTestLectureWithCourseId(course.getId());
		
		assertFalse(course.getLectures().contains(lecture));
		
		course.addLecture(lecture);
		
		assertTrue(course.getLectures().contains(lecture));
	}

	@Test
	void testRemoveLecture() {
		Lecture lecture = getTestLectureWithCourseId(course.getId());
		course.addLecture(lecture);
		
		course.removeLecture(lecture);
		
		assertFalse(course.getLectures().contains(lecture));
	}
	
	@Test
	void testEqualsMethodReturnsTrueForEqualCourses() {
		Course other = getTestCourse();
		
		assertEquals(course, other);
	}
	
	@Test
	void testEqualsMethodReturnsFalseForNonEqualCourses() {
		int courseId = 1234567;
		String courseName = "Qwerty course";
		Course other = new Course(courseId, courseName);
		
		assertNotEquals(course, other);
	}
	
	@Test
	void testEqualsMethodReturnsTrueForSameInstanceCourse() {
		Course other = course;
		
		assertEquals(course, other);
	}
	
	@Test
	void testEqualsMethodReturnsFalseForNullArgument() {
		Course other = null;
		
		assertNotEquals(course, other);
	}
	
	@Test
	void testEqualsMethodReturnsFalseWithObjectArgument() {
		Object other = new Object();
		
		assertNotEquals(course, other);
	}

}
