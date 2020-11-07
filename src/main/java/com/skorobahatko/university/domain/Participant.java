package com.skorobahatko.university.domain;

import java.time.LocalDate;
import java.util.List;

public interface Participant {
	
	long getId();
	String getFirstName();
	String getLastName();
	List<Course> getCourses();
	void addCourse(Course course);
	void removeCourse(Course course);
	Timetable getDayTimetable(LocalDate date);
	Timetable getMonthTimetable(LocalDate date);

}
