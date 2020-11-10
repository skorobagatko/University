package com.skorobahatko.university.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LectureTest {
	
	private Lecture lecture;

	@BeforeEach
	void setUp() throws Exception {
		lecture = getTestLecture();
	}
	
	@Test
	void testEqualsMethodReturnsTrueForEqualLectures() {
		Lecture other = getTestLecture();
		
		assertTrue(lecture.equals(other));
	}
	
	@Test
	void testEqualsMethodReturnsFalseForNonEqualLectures() {
		Course course = new Course("QWERTY-123", "Test course 123");
		String name = "Test lecture";
		LocalDate date = LocalDate.of(2020, 12, 1);
		LocalTime startTime = LocalTime.of(9, 30);
		LocalTime endTime = LocalTime.of(11, 0);
		int roomNumber = 201;
		Lecture other = new Lecture(name, course, date, startTime, endTime, roomNumber);
		
		assertTrue(!lecture.equals(other));
	}
	
	@Test
	void testEqualsMethodReturnsTrueForSameLectures() {
		Lecture other = lecture;
		
		assertTrue(lecture.equals(other));
	}
	
	@Test
	void testEqualsMethodReturnsFalseForNullArgument() {
		Lecture other = null;
		
		assertTrue(!lecture.equals(other));
	}
	
	@Test
	void testEqualsMethodReturnsFalseWithObjectArgument() {
		Object other = new Object();
		
		assertTrue(!lecture.equals(other));
	}
	
	private Lecture getTestLecture() {
		Course course = new Course("TST-201", "Test course");
		String name = "Test lecture";
		LocalDate date = LocalDate.of(2020, 11, 5);
		LocalTime startTime = LocalTime.of(7, 30);
		LocalTime endTime = LocalTime.of(9, 0);
		int roomNumber = 201;
		return new Lecture(name, course, date, startTime, endTime, roomNumber);
	}

}
