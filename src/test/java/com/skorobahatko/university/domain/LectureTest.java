package com.skorobahatko.university.domain;

import static com.skorobahatko.university.util.TestUtils.*;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LectureTest {
	
	private Lecture lecture;

	@BeforeEach
	void setUp() throws Exception {
		lecture = getTestLectureWithCourseId(1);
	}
	
	@Test
	void testEqualsMethodReturnsTrueForEqualLectures() {
		Lecture other = getTestLectureWithCourseId(1);
		
		assertEquals(lecture, other);
	}
	
	@Test
	void testEqualsMethodReturnsFalseForNonEqualLectures() {
		int id = 123456;
		int courseId = 1;
		String name = "Test lecture";
		LocalDate date = LocalDate.of(2020, 12, 1);
		LocalTime startTime = LocalTime.of(9, 30);
		LocalTime endTime = LocalTime.of(11, 0);
		int roomNumber = 201;
		Lecture other = new Lecture(id, name, courseId, date, startTime, endTime, roomNumber);
		
		assertNotEquals(lecture, other);
	}
	
	@Test
	void testEqualsMethodReturnsTrueForSameLectures() {
		Lecture other = lecture;
		
		assertEquals(lecture, other);
	}
	
	@Test
	void testEqualsMethodReturnsFalseForNullArgument() {
		Lecture other = null;
		
		assertNotEquals(lecture, other);
	}
	
	@Test
	void testEqualsMethodReturnsFalseWithObjectArgument() {
		Object other = new Object();
		
		assertNotEquals(lecture, other);
	}
	
}
