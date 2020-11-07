package com.skorobahatko.university.domain;

import java.util.List;

public class UniversityManagement {
	
	public UniversityManagement() {
		// NOP
	}
	
	public Course createCourse(String id, String name, List<Lecture> lectures) {
		return new Course(id, name, lectures);
	}

}
