package com.skorobahatko.university.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
	void testGetDayTimetable() throws Exception {
		List<Course> courses = getTestCourses();
		LocalDate startDate = LocalDate.of(2020, 11, 5);
		LocalDate endDate = LocalDate.of(2020, 11, 5);
		
		Constructor<Timetable> constructor = Timetable.class.getDeclaredConstructor(List.class, LocalDate.class, LocalDate.class);
		constructor.setAccessible(true);
		Timetable expected = constructor.newInstance(courses, startDate, endDate);
		
		Participant participant = getTestParticipant();
		Timetable actual = Timetable.getDayTimetable(participant, startDate);
		
		assertEquals(expected, actual);
	}

	@Test
	void testGetMonthTimetable() throws Exception {
		List<Course> courses = getTestCourses();
		LocalDate startDate = LocalDate.of(2020, 11, 5);
		LocalDate endDate = startDate.plusMonths(1);
		
		Constructor<Timetable> constructor = Timetable.class.getDeclaredConstructor(List.class, LocalDate.class, LocalDate.class);
		constructor.setAccessible(true);
		Timetable expected = constructor.newInstance(courses, startDate, endDate);
		
		Participant participant = getTestParticipant();
		Timetable actual = Timetable.getMonthTimetable(participant, startDate);
		
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
	
	private Participant getTestParticipant() {
		long studentId = 123;
		String studentFirstName = "Test";
		String studentLastName = "Test";
		List<Course> studentCourses = getTestCourses();
		return new Student(studentId, studentFirstName, studentLastName, studentCourses);
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
