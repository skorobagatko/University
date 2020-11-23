package com.skorobahatko.university.dao.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.skorobahatko.university.domain.Course;
import com.skorobahatko.university.domain.Lecture;
import com.skorobahatko.university.domain.Participant;
import com.skorobahatko.university.domain.Student;

public class DaoTestUtils {
	
	public static Course getTestCourse() {
		Course course = new Course(1, "Test Course");
		return course;
	}
	
	public static List<Lecture> getTestLecturesWith(int courseId) {
		List<Lecture> lectures = new ArrayList<>();
		lectures.add(new Lecture(1, "Lecture 1", courseId, LocalDate.of(2020, 12, 1), LocalTime.of(7, 30), LocalTime.of(9, 00), 100));
		lectures.add(new Lecture(2, "Lecture 2", courseId, LocalDate.of(2020, 12, 3), LocalTime.of(9, 30), LocalTime.of(11, 00), 100));
		lectures.add(new Lecture(3, "Lecture 3", courseId, LocalDate.of(2020, 12, 7), LocalTime.of(7, 30), LocalTime.of(9, 00), 100));
		return lectures;
	}
	
	public static Participant getTestParticipant() {
		int id = 1;
		String firstName = "John";
		String lastName = "Johnson";

		return new Student(id, firstName, lastName);
	}

}
