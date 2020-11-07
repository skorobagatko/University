package com.skorobahatko.university.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TimetableRecordTest {
	
	private TimetableRecord record;

	@BeforeEach
	void setUp() throws Exception {
		record = getTestTimetableRecord();
	}

	@Test
	void testGetDate() {
		LocalDate expected = LocalDate.of(2020, 11, 5);
		LocalDate actual = record.getDate();
		
		assertEquals(expected, actual);
	}

	@Test
	void testGetLectureName() {
		String expected = "Test lecture";
		String actual = record.getLectureName();
		
		assertEquals(expected, actual);
	}

	@Test
	void testGetStartTime() {
		LocalTime expected = LocalTime.of(7, 30);
		LocalTime actual = record.getStartTime();
		
		assertEquals(expected, actual);
	}

	@Test
	void testGetEndTime() {
		LocalTime expected = LocalTime.of(9, 0);
		LocalTime actual = record.getEndTime();
		
		assertEquals(expected, actual);
	}

	@Test
	void testGetRoomNumber() {
		int expected = 201;
		int actual = record.getRoomNumber();
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testEqualsMethodReturnsTrueForEqualTimetableRecords() {
		TimetableRecord other = getTestTimetableRecord();
		
		assertTrue(record.equals(other));
	}
	
	@Test
	void testEqualsMethodReturnsFalseForNonEqualTimetableRecords() {
		record = getTestTimetableRecord();
		
		Course course = new Course("QWERTY-100", "Qwerty course");
		String lectureName = "Qwerty lecture";
		LocalDate lectureDate = LocalDate.of(2021, 12, 1);
		LocalTime lectureStartTime = LocalTime.of(9, 30);
		LocalTime lectureEndTime = LocalTime.of(11, 0);
		int roomNumber = 201;
		Lecture lecture = new Lecture(lectureName, course, lectureDate, lectureStartTime, lectureEndTime, roomNumber);
		TimetableRecord other = TimetableRecord.of(lecture);
		
		assertTrue(!record.equals(other));
	}
	
	@Test
	void testHashcodeMethodReturnsSameHashcodeForEqualTimetableRecords() {
		record = getTestTimetableRecord();
		TimetableRecord other = getTestTimetableRecord();
		
		assertEquals(record.hashCode(), other.hashCode());
	}
	
	@Test
	void testHashcodeMethodReturnsDifferentHashcodeForNonEqualTimetableRecords() {
		record = getTestTimetableRecord();
		
		Course course = new Course("QWERTY-100", "Qwerty course");
		String lectureName = "Qwerty lecture";
		LocalDate lectureDate = LocalDate.of(2021, 12, 1);
		LocalTime lectureStartTime = LocalTime.of(9, 30);
		LocalTime lectureEndTime = LocalTime.of(11, 0);
		int roomNumber = 201;
		Lecture lecture = new Lecture(lectureName, course, lectureDate, lectureStartTime, lectureEndTime, roomNumber);
		TimetableRecord other = TimetableRecord.of(lecture);
		
		assertNotEquals(course.hashCode(), other.hashCode());
	}
	
	@Test
	void testToString() {
		String expected = "TimetableRecord [date=2020-11-05, lectureName=Test lecture, "
				+ "startTime=07:30, endTime=09:00, roomNumber=201]";
		
		String actual = record.toString();
		
		assertEquals(expected, actual);
	}
	
	
	private TimetableRecord getTestTimetableRecord() {
		Course course = new Course("TST-100", "Test course");
		String lectureName = "Test lecture";
		LocalDate lectureDate = LocalDate.of(2020, 11, 5);
		LocalTime lectureStartTime = LocalTime.of(7, 30);
		LocalTime lectureEndTime = LocalTime.of(9, 0);
		int roomNumber = 201;
		Lecture lecture = new Lecture(lectureName, course, lectureDate, lectureStartTime, lectureEndTime, roomNumber);
		TimetableRecord result = TimetableRecord.of(lecture);
		
		return result;
	}

}
