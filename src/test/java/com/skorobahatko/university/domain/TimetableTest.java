package com.skorobahatko.university.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TimetableTest {
	
	private Timetable timetable;

	@BeforeEach
	void setUp() throws Exception {
		timetable = getTestTimetable();
	}

	@Test
	void testGetStartDate() {
		LocalDate expected = LocalDate.of(2020, 11, 5);
		LocalDate actual = timetable.getStartDate();
		
		assertEquals(expected, actual);
	}

	@Test
	void testGetEndDate() {
		LocalDate expected = LocalDate.of(2020, 11, 7);
		LocalDate actual = timetable.getEndDate();
		
		assertEquals(expected, actual);
	}

	@Test
	void testGetRecords() {
		List<TimetableRecord> expected = getTestCourses()
				.stream()
				.flatMap(course -> course.getLectures().stream())
				.map(TimetableRecord::of)
				.collect(Collectors.toList());
		
		List<TimetableRecord> actual = timetable.getRecords();
		
		assertEquals(expected, actual);
	}

	@Test
	void testAddRecord() {
		int recordsNumber = timetable.getRecords().size();
		int expected = recordsNumber + 1;
		
		Course course = new Course("QWERTY", "Test course");
		String lectureName = "Test lecture";
		LocalDate lectureDate = LocalDate.of(2020, 12, 5);
		LocalTime lectureStartTime = LocalTime.of(9, 30);
		LocalTime lectureEndTime = LocalTime.of(11, 0);
		int lectureRoomNumber = 300;
		Lecture lecture = new Lecture(lectureName, course, lectureDate, lectureStartTime, lectureEndTime, lectureRoomNumber);
		
		timetable.addRecord(TimetableRecord.of(lecture));
		
		int actual = timetable.getRecords().size();
		
		assertEquals(expected, actual);
	}

	@Test
	void testRemoveRecord() {
		int recordsNumber = timetable.getRecords().size();
		int expected = recordsNumber - 1;
		
		Course course = new Course("TST-101", "Test course 1");
		String lectureName = "Test lecture 1";
		LocalDate lectureDate = LocalDate.of(2020, 11, 5);
		LocalTime lectureStartTime = LocalTime.of(7, 30);
		LocalTime lectureEndTime = LocalTime.of(9, 0);
		int lectureRoomNumber = 101;
		Lecture lecture = new Lecture(lectureName, course, lectureDate, lectureStartTime, lectureEndTime, lectureRoomNumber);
		
		timetable.removeRecord(TimetableRecord.of(lecture));
		int actual = timetable.getRecords().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testEqualsMethodReturnsTrueForEqualTimetables() {
		Timetable other = getTestTimetable();
		
		assertTrue(timetable.equals(other));
	}
	
	@Test
	void testEqualsMethodReturnsFalseForNonEqualTimetables() {
		List<Course> testCourses = getTestCourses();
		LocalDate startDate = LocalDate.of(2021, 12, 6);
		LocalDate endDate = LocalDate.of(2021, 12, 8);
		Timetable other = new Timetable(testCourses, startDate, endDate);
		
		assertTrue(!timetable.equals(other));
	}
	
	@Test
	void testEqualsMethodReturnsTrueForSameTimetables() {
		Timetable other = timetable;
		
		assertTrue(timetable.equals(other));
	}
	
	@Test
	void testEqualsMethodReturnsFalseForNullArgument() {
		Timetable other = null;
		
		assertTrue(!timetable.equals(other));
	}
	
	@Test
	void testEqualsMethodReturnsFalseWithObjectArgument() {
		Object other = new Object();
		
		assertTrue(!timetable.equals(other));
	}
	
	@Test
	void testHashcodeMethodReturnsSameHashcodeForEqualTimetable() {
		Timetable other = getTestTimetable();
		
		assertEquals(timetable.hashCode(), other.hashCode());
	}
	
	@Test
	void testHashcodeMethodReturnsDifferentHashcodeForNonEqualTimetable() {
		List<Course> testCourses = getTestCourses();
		LocalDate startDate = LocalDate.of(2021, 12, 6);
		LocalDate endDate = LocalDate.of(2021, 12, 8);
		Timetable other = new Timetable(testCourses, startDate, endDate);
		
		assertNotEquals(timetable.hashCode(), other.hashCode());
	}
	
	@Test
	void testToString() {
		String expected = "Timetable [startDate=2020-11-05, endDate=2020-11-07, "
				+ "records=[TimetableRecord [date=2020-11-05, lectureName=Test lecture 1, "
				+ "startTime=07:30, endTime=09:00, roomNumber=101], "
				+ "TimetableRecord [date=2020-11-06, lectureName=Test lecture 2, "
				+ "startTime=10:30, endTime=12:00, roomNumber=201], TimetableRecord [date=2020-11-07, "
				+ "lectureName=Test lecture 3, startTime=14:30, endTime=16:00, roomNumber=301]]]";
		
		String actual = timetable.toString();
		
		assertEquals(expected, actual);
	}
	
	private Timetable getTestTimetable() {
		List<Course> testCourses = getTestCourses();
		LocalDate startDate = LocalDate.of(2020, 11, 5);
		LocalDate endDate = LocalDate.of(2020, 11, 7);
		return new Timetable(testCourses, startDate, endDate);
	}
	
	private List<Course> getTestCourses() {
		Course course = new Course("TST-101", "Test course 1");
		
		String lectureName = "Test lecture 1";
		LocalDate lectureDate = LocalDate.of(2020, 11, 5);
		LocalTime lectureStartTime = LocalTime.of(7, 30);
		LocalTime lectureEndTime = LocalTime.of(9, 0);
		int lectureRoomNumber = 101;
		Lecture lecture = new Lecture(lectureName, course, lectureDate, lectureStartTime, lectureEndTime, lectureRoomNumber);
		course.addLecture(lecture);
		
		lectureName = "Test lecture 2";
		lectureDate = LocalDate.of(2020, 11, 6);
		lectureStartTime = LocalTime.of(10, 30);
		lectureEndTime = LocalTime.of(12, 0);
		lectureRoomNumber = 201;
		lecture = new Lecture(lectureName, course, lectureDate, lectureStartTime, lectureEndTime, lectureRoomNumber);
		course.addLecture(lecture);
		
		lectureName = "Test lecture 3";
		lectureDate = LocalDate.of(2020, 11, 7);
		lectureStartTime = LocalTime.of(14, 30);
		lectureEndTime = LocalTime.of(16, 0);
		lectureRoomNumber = 301;
		lecture = new Lecture(lectureName, course, lectureDate, lectureStartTime, lectureEndTime, lectureRoomNumber);
		course.addLecture(lecture);
		
		return List.of(course);
	}

}
