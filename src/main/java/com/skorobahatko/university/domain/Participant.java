package com.skorobahatko.university.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Participant {
	
	private int id;
	private String firstName;
	private String lastName;
	private List<Course> courses;
	
	public Participant() {
		courses = new ArrayList<>();
	}
	
	public Participant(String firstName, String lastName) {
		this(0, firstName, lastName, new ArrayList<>());
	}
	
	public Participant(int id, String firstName, String lastName, List<Course> courses) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.courses = courses;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public List<Course> getCourses() {
		return courses;
	}
	
	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}
	
	public void addCourse(Course course) {
		courses.add(course);
	}
	
	public void removeCourse(Course course) {
		courses.remove(course);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((courses == null) ? 0 : courses.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + id;
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		Participant other = (Participant) obj;
		
		return id == other.id &&
				Objects.equals(firstName, other.firstName) &&
				Objects.equals(lastName, other.lastName) &&
				Objects.equals(courses, other.courses);
	}

	@Override
	public String toString() {
		return "Participant [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", courses=" + courses
				+ "]";
	}
	
}
