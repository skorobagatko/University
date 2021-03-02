package com.skorobahatko.university.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.skorobahatko.university.domain.Course;
import com.skorobahatko.university.domain.Lecture;
import com.skorobahatko.university.domain.Participant;
import com.skorobahatko.university.domain.Student;
import com.skorobahatko.university.domain.Teacher;
import com.skorobahatko.university.domain.Timetable;

public class TestUtils {
	
	private TestUtils() {}

	public static Course getTestCourse() {
		return new Course("Test Course");
	}
	
	public static Lecture getTestLectureWithCourseId(int courseId) {
		return new Lecture("Lecture 1", courseId, LocalDate.of(2020, 12, 1), LocalTime.of(8, 00),
				LocalTime.of(9, 00), 100);
	}

	public static List<Lecture> getTestLecturesWithCourseId(int courseId) {
		List<Lecture> lectures = new ArrayList<>();
		lectures.add(new Lecture("Lecture 1", courseId, LocalDate.of(2020, 12, 1), LocalTime.of(8, 00),
				LocalTime.of(9, 00), 100));
		lectures.add(new Lecture("Lecture 2", courseId, LocalDate.of(2020, 12, 3), LocalTime.of(10, 00),
				LocalTime.of(11, 00), 100));
		lectures.add(new Lecture("Lecture 3", courseId, LocalDate.of(2020, 12, 7), LocalTime.of(8, 00),
				LocalTime.of(9, 00), 100));
		return lectures;
	}
	
	public static Student getTestStudent() {
		String firstName = "John";
		String lastName = "Johnson";

		return new Student(firstName, lastName);
	}
	
	public static Teacher getTestTeacher() {
		String firstName = "John";
		String lastName = "Johnson";

		return new Teacher(firstName, lastName);
	}

	public static Participant getTestParticipant() {
		return getTestStudent();
	}
	
	public static List<Student> getTestStudents() {
		List<Student> students = new ArrayList<>();
		students.add(new Student("John", "Johnson"));
		students.add(new Student("Andrew", "Anderson"));
		students.add(new Student("Harry", "Harrison"));
		
		return students;
	}
	
	public static List<Teacher> getTestTeachers() {
		List<Teacher> teachers = new ArrayList<>();
		teachers.add(new Teacher("John", "Johnson"));
		teachers.add(new Teacher("Andrew", "Anderson"));
		teachers.add(new Teacher("Harry", "Harrison"));
		
		return teachers;
	}

	public static Timetable getTestTimetableForParticipant(Participant participant) {
		LocalDate startDate = LocalDate.of(2020, 12, 1);
		return Timetable.getMonthTimetable(participant, startDate);
	}

}
