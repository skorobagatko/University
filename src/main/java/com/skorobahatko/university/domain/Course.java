package com.skorobahatko.university.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Course {
	
	private int id;
	private String name;
	private List<Lecture> lectures;
	
	public Course(String name) {
		this(0, name);
	}
	
	public Course(int id, String name) {
		this(id, name, new ArrayList<>());
	}
	
	public Course(String name, List<Lecture> lectures) {
		this(0, name, lectures);
	}
	
	public Course(int id, String name, List<Lecture> lectures) {
		this.id = id;
		this.name = name;
		this.lectures = lectures;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public List<Lecture> getLectures() {
		return lectures;
	}
	
	public void setLectures(List<Lecture> lectures) {
		this.lectures = lectures;
	}
	
	public void addLecture(Lecture lecture) {
		lectures.add(lecture);
	}
	
	public void removeLecture(Lecture lecture) {
		lectures.remove(lecture);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((lectures == null) ? 0 : lectures.hashCode());
		
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
		
		Course other = (Course) obj;
		
		return id == other.id &&
				Objects.equals(name, other.name) &&
				Objects.equals(lectures, other.lectures);
	}

	@Override
	public String toString() {
		return "Course [id=" + id + ", name=" + name + ", lectures=" + lectures + "]";
	}
	
}