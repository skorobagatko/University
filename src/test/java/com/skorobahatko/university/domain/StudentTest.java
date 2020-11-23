package com.skorobahatko.university.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentTest {
	
	private Student student;

	@BeforeEach
	void setUp() throws Exception {
		student = getTestStudent();
	}

	@Test
	void testAddCourse() {
		Course course = new Course(123, "Test course 123");
		
		assertTrue(!student.getCourses().contains(course));
		
		student.addCourse(course);
		
		assertTrue(student.getCourses().contains(course));
	}

	@Test
	void testRemoveCourse() {
		Course course = new Course(456, "Test course 456");
		student.addCourse(course);
		
		student.removeCourse(course);
		
		assertTrue(!student.getCourses().contains(course));
	}
	
	@Test
	void testEqualsMethodReturnsTrueForEqualStudents() {
		Student other = getTestStudent();
		
		assertTrue(student.equals(other));
	}
	
	@Test
	void testEqualsMethodReturnsFalseForNonEqualStudents() {
		int id = 123456789;
		String firstName = "Test";
		String lastName = "Test";
		List<Course> courses = getTestCourses();
		Student other = new Student(id, firstName, lastName, courses);
		
		assertTrue(!student.equals(other));
	}
	
	@Test
	void testEqualsMethodReturnsTrueForSameStudents() {
		Student other = student;
		
		assertTrue(student.equals(other));
	}
	
	@Test
	void testEqualsMethodReturnsFalseForNullArgument() {
		Student other = null;
		
		assertTrue(!student.equals(other));
	}
	
	@Test
	void testEqualsMethodReturnsFalseWithObjectArgument() {
		Object other = new Object();
		
		assertTrue(!student.equals(other));
	}
	
	private List<Course> getTestCourses() {
		List<Course> result = new ArrayList<>();
		
		Course course = new Course(1, "Test course 1");
		
		int id = 1;
		String lectureName = "Test lecture 1";
		LocalDate lectureDate = LocalDate.of(2020, 11, 5);
		LocalTime lectureStartTime = LocalTime.of(7, 30);
		LocalTime lectureEndTime = LocalTime.of(9, 0);
		int lectureRoomNumber = 101;
		Lecture lecture = new Lecture(id, lectureName, course.getId(), lectureDate, lectureStartTime, lectureEndTime, lectureRoomNumber);
		course.addLecture(lecture);
		
		id = 2;
		lectureName = "Test lecture 2";
		lectureDate = LocalDate.of(2020, 11, 6);
		lectureStartTime = LocalTime.of(10, 30);
		lectureEndTime = LocalTime.of(12, 0);
		lectureRoomNumber = 201;
		lecture = new Lecture(id, lectureName, course.getId(), lectureDate, lectureStartTime, lectureEndTime, lectureRoomNumber);
		course.addLecture(lecture);
		
		id = 3;
		lectureName = "Test lecture 3";
		lectureDate = LocalDate.of(2020, 11, 7);
		lectureStartTime = LocalTime.of(14, 30);
		lectureEndTime = LocalTime.of(16, 0);
		lectureRoomNumber = 301;
		lecture = new Lecture(id, lectureName, course.getId(), lectureDate, lectureStartTime, lectureEndTime, lectureRoomNumber);
		course.addLecture(lecture);
		
		result.add(course);
		
		return result;
	}
	
	private Student getTestStudent() {
		int id = 1;
		String firstName = "John";
		String lastName = "Johnson";
		List<Course> courses = getTestCourses();
		
		return new Student(id, firstName, lastName, courses);
	}

}
