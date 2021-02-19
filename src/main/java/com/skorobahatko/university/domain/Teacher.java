package com.skorobahatko.university.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@DiscriminatorValue("Teacher")
public class Teacher extends Participant {
	
	public Teacher() {}

	public Teacher(String firstName, String lastName) {
		this(0, firstName, lastName);
	}
	
	public Teacher(Integer id, String firstName, String lastName) {
		super(id, firstName, lastName, new ArrayList<>());
	}
	
	public Teacher(String firstName, String lastName, List<Course> courses) {
		super(0, firstName, lastName, courses);
	}
	
	public Teacher(Integer id, String firstName, String lastName, List<Course> courses) {
		super(id, firstName, lastName, courses);
	}

	@Override
	public String toString() {
		return "Teacher [" + super.toString() + "]";
	}
	
}
