package com.skorobahatko.university.domain;

import static com.skorobahatko.university.util.TestUtils.*;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TimetableTest {
	
	private Timetable timetable;

	@BeforeEach
	void setUp() {
		Participant participant = getTestParticipant();
		timetable = getTestTimetableForParticipant(participant);
	}
	
	@Test
	void testGetDayTimetable() throws Exception {
		Participant participant = getTestParticipant();
		LocalDate date = LocalDate.of(2020, 11, 5);
		
		Timetable timetable = Timetable.getDayTimetable(participant, date);
		
		assertEquals(date, timetable.getStartDate());
		assertEquals(date, timetable.getEndDate());
	}
	
	@Test
	void testGetMonthTimetable() throws Exception {
		Participant participant = getTestParticipant();
		LocalDate date = LocalDate.of(2020, 11, 5);
		
		Timetable timetable = Timetable.getMonthTimetable(participant, date);
		
		assertEquals(date, timetable.getStartDate());
		assertEquals(date.plusMonths(1), timetable.getEndDate());
	}
	
	@Test
	void testEqualsMethodReturnsTrueForEqualTimetables() {
		Participant participant = getTestParticipant();
		Timetable other = getTestTimetableForParticipant(participant);
		
		assertEquals(timetable, other);
	}
	
	@Test
	void testEqualsMethodReturnsFalseForNonEqualTimetables() {
		int studentId = 123;
		String firstName = "Test";
		String lastName = "Test";
		Participant participant = new Student(studentId, firstName, lastName);

		Timetable other = getTestTimetableForParticipant(participant);
		
		assertNotEquals(timetable, other);
	}
	
	@Test
	void testEqualsMethodReturnsTrueForSameTimetables() {
		Timetable other = timetable;
		
		assertEquals(timetable, other);
	}
	
	@Test
	void testEqualsMethodReturnsFalseForNullArgument() {
		Timetable other = null;
		
		assertNotEquals(timetable, other);
	}
	
	@Test
	void testEqualsMethodReturnsFalseWithObjectArgument() {
		Object other = new Object();
		
		assertNotEquals(timetable, other);
	}
	
	@Test
	void testGetDayTimetableThrowsExceptionForNullParticipantArgument() {
		assertThrows(IllegalArgumentException.class, 
				() -> Timetable.getDayTimetable(null));
	}
	
	@Test
	void testGetDayTimetableThrowsExceptionForNullDateArgument() {
		Participant participant = getTestParticipant();
		assertThrows(IllegalArgumentException.class, 
				() -> Timetable.getDayTimetable(participant, null));
	}
	
	@Test
	void testGetMonthTimetableThrowsExceptionForNullParticipantArgument() {
		assertThrows(IllegalArgumentException.class, 
				() -> Timetable.getMonthTimetable(null));
	}
	
	@Test
	void testGetMonthTimetableThrowsExceptionForNullDateArgument() {
		Participant participant = getTestParticipant();
		assertThrows(NullPointerException.class, 
				() -> Timetable.getMonthTimetable(participant, null));
	}

}
