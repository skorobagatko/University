package com.skorobahatko.university.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UniversityManagementTest {
	
	private UniversityManagement management;

	@BeforeEach
	void setUp() throws Exception {
		management = new UniversityManagement();
	}

	@Test
	void testCreateCourse() {
		String courseId = "TST-101";
		String courseName = "Test course 1";
		Course expected = new Course(courseId, courseName);
		String lectureName = "Test lecture 1";
		LocalDate lectureDate = LocalDate.of(2020, 11, 5);
		LocalTime lectureStartTime = LocalTime.of(7, 30);
		LocalTime lectureEndTime = LocalTime.of(9, 0);
		int lectureRoomNumber = 101;
		Lecture lecture = new Lecture(lectureName, expected, lectureDate, lectureStartTime, lectureEndTime, lectureRoomNumber);
		expected.addLecture(lecture);
		
		Course actual = management.createCourse(courseId, courseName, List.of(lecture));
		
		assertEquals(expected, actual);
	}

}
