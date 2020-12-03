package com.skorobahatko.university.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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
		Participant participant = getTestParticipant();
		LocalDate startDate = LocalDate.of(2020, 11, 5);
		LocalDate endDate = LocalDate.of(2020, 11, 5);
		
		Constructor<Timetable> constructor = Timetable.class.getDeclaredConstructor(Participant.class, LocalDate.class, LocalDate.class);
		constructor.setAccessible(true);
		Timetable expected = constructor.newInstance(participant, startDate, endDate);
		
		
		Timetable actual = Timetable.getDayTimetable(participant, startDate);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetMonthTimetable() throws Exception {
		Participant participant = getTestParticipant();
		LocalDate startDate = LocalDate.of(2020, 11, 5);
		LocalDate endDate = startDate.plusMonths(1);
		
		Constructor<Timetable> constructor = Timetable.class.getDeclaredConstructor(Participant.class, LocalDate.class, LocalDate.class);
		constructor.setAccessible(true);
		Timetable expected = constructor.newInstance(participant, startDate, endDate);
		
		Timetable actual = Timetable.getMonthTimetable(participant, startDate);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testAddLecture() {
		int lecturesNumber = timetable.getLectures().size();
		int expected = lecturesNumber + 1;
		
		int lectureId = 123;
		int courseId = 1;
		String lectureName = "Test lecture";
		LocalDate lectureDate = LocalDate.of(2020, 12, 5);
		LocalTime lectureStartTime = LocalTime.of(9, 30);
		LocalTime lectureEndTime = LocalTime.of(11, 0);
		int lectureRoomNumber = 300;
		Lecture lecture = new Lecture(lectureId, lectureName, courseId, lectureDate, lectureStartTime, lectureEndTime, lectureRoomNumber);
		
		timetable.addLecture(lecture);
		
		int actual = timetable.getLectures().size();
		
		assertEquals(expected, actual);
	}

	@Test
	void testRemoveLecture() {
		int lecturesNumber = timetable.getLectures().size();
		int expected = lecturesNumber - 1;
		
		int lectureId = 1;
		int courseId = 1;
		String lectureName = "Test lecture 1";
		LocalDate lectureDate = LocalDate.of(2020, 11, 5);
		LocalTime lectureStartTime = LocalTime.of(7, 30);
		LocalTime lectureEndTime = LocalTime.of(9, 0);
		int lectureRoomNumber = 101;
		Lecture lecture = new Lecture(lectureId, lectureName, courseId, lectureDate, lectureStartTime, lectureEndTime, lectureRoomNumber);
		
		timetable.removeLecture(lecture);
		int actual = timetable.getLectures().size();
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testEqualsMethodReturnsTrueForEqualTimetables() {
		Timetable other = getTestTimetable();
		
		assertTrue(timetable.equals(other));
	}
	
	@Test
	void testEqualsMethodReturnsFalseForNonEqualTimetables() {
		int studentId = 123;
		String studentFirstName = "Test";
		String studentLastName = "Test";
		List<Course> studentCourses = getTestCourses();
		Participant participant = new Student(studentId, studentFirstName, studentLastName, studentCourses);

		LocalDate startDate = LocalDate.of(2021, 12, 6);
		Timetable other = Timetable.getDayTimetable(participant, startDate);
		
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
		int studentId = 123;
		String studentFirstName = "Test";
		String studentLastName = "Test";
		List<Course> studentCourses = getTestCourses();
		return new Student(studentId, studentFirstName, studentLastName, studentCourses);
	}
	
	private Timetable getTestTimetable() {
		int studentId = 123;
		String studentFirstName = "Test";
		String studentLastName = "Test";
		List<Course> studentCourses = getTestCourses();
		Participant participant = new Student(studentId, studentFirstName, studentLastName, studentCourses);
		
		LocalDate startDate = LocalDate.of(2020, 11, 5);
		return Timetable.getDayTimetable(participant, startDate);
	}
	
	private List<Course> getTestCourses() {
		Course course = new Course(1, "Test course 1");
		
		int lectureId = 1;
		String lectureName = "Test lecture 1";
		LocalDate lectureDate = LocalDate.of(2020, 11, 5);
		LocalTime lectureStartTime = LocalTime.of(7, 30);
		LocalTime lectureEndTime = LocalTime.of(9, 0);
		int lectureRoomNumber = 101;
		Lecture lecture = new Lecture(lectureId, lectureName, course.getId(), lectureDate, lectureStartTime, lectureEndTime, lectureRoomNumber);
		course.addLecture(lecture);
		
		lectureId = 2;
		lectureName = "Test lecture 2";
		lectureDate = LocalDate.of(2020, 11, 6);
		lectureStartTime = LocalTime.of(10, 30);
		lectureEndTime = LocalTime.of(12, 0);
		lectureRoomNumber = 201;
		lecture = new Lecture(lectureId, lectureName, course.getId(), lectureDate, lectureStartTime, lectureEndTime, lectureRoomNumber);
		course.addLecture(lecture);
		
		lectureId = 3;
		lectureName = "Test lecture 3";
		lectureDate = LocalDate.of(2020, 11, 7);
		lectureStartTime = LocalTime.of(14, 30);
		lectureEndTime = LocalTime.of(16, 0);
		lectureRoomNumber = 301;
		lecture = new Lecture(lectureId, lectureName, course.getId(), lectureDate, lectureStartTime, lectureEndTime, lectureRoomNumber);
		course.addLecture(lecture);
		
		return List.of(course);
	}

}