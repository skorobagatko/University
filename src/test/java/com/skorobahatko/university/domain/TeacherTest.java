package com.skorobahatko.university.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TeacherTest {
	
	private Teacher teacher;

	@BeforeEach
	void setUp() throws Exception {
		teacher = getTestTeacher();
	}

	@Test
	void testGetDayTimetable() {
		List<Course> courses = getTestCourses();
		LocalDate startDate = LocalDate.of(2020, 11, 5);
		LocalDate endDate = startDate;
		Timetable expected = new Timetable(courses, startDate, endDate);
		
		Timetable actual = teacher.getDayTimetable(startDate);
		
		assertEquals(expected, actual);
	}

	@Test
	void testGetMonthTimetable() {
		List<Course> courses = getTestCourses();
		LocalDate startDate = LocalDate.of(2020, 11, 1);
		LocalDate endDate = LocalDate.of(2020, 12, 1);
		Timetable expected = new Timetable(courses, startDate, endDate);
		
		Timetable actual = teacher.getMonthTimetable(startDate);
		
		assertEquals(expected, actual);
	}

	@Test
	void testGetId() {
		long expected = 1;
		long actual = teacher.getId();
		
		assertEquals(expected, actual);
	}

	@Test
	void testGetFirstName() {
		String expected = "John";
		
		String actual = teacher.getFirstName();
		
		assertEquals(expected, actual);
	}

	@Test
	void testGetLastName() {
		String expected = "Johnson";
		
		String actual = teacher.getLastName();
		
		assertEquals(expected, actual);
	}

	@Test
	void testGetCourses() {
		long id = 1;
		String firstName = "Test";
		String lastName = "Test";
		teacher = new Teacher(id, firstName, lastName);
		
		Course course = new Course("TST-101", "Test course 1");
		String lectureName = "Test lecture 1";
		LocalDate lectureDate = LocalDate.of(2020, 11, 5);
		LocalTime lectureStartTime = LocalTime.of(7, 30);
		LocalTime lectureEndTime = LocalTime.of(9, 0);
		int lectureRoomNumber = 101;
		Lecture lecture = new Lecture(lectureName, course, lectureDate, lectureStartTime, lectureEndTime, lectureRoomNumber);
		course.addLecture(lecture);
		teacher.addCourse(course);
		
		List<Course> expected = List.of(course);
		
		List<Course> actual = teacher.getCourses();
		
		assertEquals(expected, actual);
	}

	@Test
	void testAddCourse() {
		Course course = new Course("QWERTY123", "Test course 123");
		
		assertTrue(!teacher.getCourses().contains(course));
		
		teacher.addCourse(course);
		
		assertTrue(teacher.getCourses().contains(course));
	}

	@Test
	void testRemoveCourse() {
		Course course = new Course("QWERTY456", "Test course 456");
		teacher.addCourse(course);
		
		teacher.removeCourse(course);
		
		assertTrue(!teacher.getCourses().contains(course));
	}
	
	@Test
	void testEqualsMethodReturnsTrueForEqualTeachers() {
		Teacher other = getTestTeacher();
		
		assertTrue(teacher.equals(other));
	}
	
	@Test
	void testEqualsMethodReturnsFalseForNonEqualTeachers() {
		long id = 123456789;
		String firstName = "Test";
		String lastName = "Test";
		List<Course> courses = getTestCourses();
		Teacher other = new Teacher(id, firstName, lastName, courses);
		
		assertTrue(!teacher.equals(other));
	}
	
	@Test
	void testHashcodeMethodReturnsSameHashcodeForEqualTeachers() {
		Teacher other = getTestTeacher();
		
		assertEquals(teacher.hashCode(), other.hashCode());
	}
	
	@Test
	void testHashcodeMethodReturnsDifferentHashcodeForNonEqualTeachers() {
		long id = 123456789;
		String firstName = "Test";
		String lastName = "Test";
		List<Course> courses = getTestCourses();
		Teacher other = new Teacher(id, firstName, lastName, courses);
		
		assertNotEquals(teacher.hashCode(), other.hashCode());
	}
	
	@Test
	void testToString() {
		String expected = "Teacher [id=1, firstName=John, lastName=Johnson, "
				+ "courses=[Course [id=TST-101, name=Test course 1, "
				+ "lectures=[Lecture [courseId=TST-101, name=Test lecture 1, "
				+ "date=2020-11-05, startTime=07:30, endTime=09:00, roomNumber=101], "
				+ "Lecture [courseId=TST-101, name=Test lecture 2, date=2020-11-06, "
				+ "startTime=10:30, endTime=12:00, roomNumber=201], "
				+ "Lecture [courseId=TST-101, name=Test lecture 3, date=2020-11-07, "
				+ "startTime=14:30, endTime=16:00, roomNumber=301]]]]]";
		
		String actual = teacher.toString();
		
		assertEquals(expected, actual);
	}
	
	private List<Course> getTestCourses() {
		List<Course> result = new ArrayList<>();
		
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
		
		result.add(course);
		
		return result;
	}
	
	private Teacher getTestTeacher() {
		long id = 1;
		String firstName = "John";
		String lastName = "Johnson";
		List<Course> courses = getTestCourses();
		
		return new Teacher(id, firstName, lastName, courses);
	}

}
