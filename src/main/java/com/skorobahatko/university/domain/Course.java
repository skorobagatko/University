package com.skorobahatko.university.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Course {
	
	private String id;
	private String name;
	private List<Lecture> lectures;
	
	Course(String id, String name) {
		this(id, name, new ArrayList<>());
	}
	
	Course(String id, String name, List<Lecture> lectures) {
		this.id = id;
		this.name = name;
		this.lectures = lectures;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Lecture> getLectures() {
		return lectures;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lectures == null) ? 0 : lectures.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		
		return Objects.equals(id, other.id) &&
				Objects.equals(name, other.name) &&
				Objects.equals(lectures, other.lectures);
	}

	@Override
	public String toString() {
		return "Course [id=" + id + ", name=" + name + ", lectures=" + lectures + "]";
	}
	
	void addLecture(Lecture lecture) {
		lectures.add(lecture);
	}
	
	void removeLecture(Lecture lecture) {
		lectures.remove(lecture);
	}
	
}
