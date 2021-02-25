package com.skorobahatko.university.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "courses")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Course {

	@Id
	@Column(name = "course_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotBlank(message = "The course name must not be empty")
	@Size(min=2, max=100, message = "The name length must be between 2 and 100")
	@Column(name = "course_name")
	private String name;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_id", updatable = false)
	private List<Lecture> lectures;

	public Course() {
		setLectures(new ArrayList<>());
	}

	public Course(String name) {
		this(0, name);
	}

	public Course(Integer id, String name) {
		this(id, name, new ArrayList<>());
	}

	public Course(String name, List<Lecture> lectures) {
		this(0, name, lectures);
	}

	public Course(Integer id, String name, List<Lecture> lectures) {
		setId(id);
		setName(name);
		setLectures(new ArrayList<>(lectures));
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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
		
		return id == other.id 
				&& Objects.equals(name, other.name)
				&& Objects.equals(lectures, other.lectures);
	}

	@Override
	public String toString() {
		return "Course [id=" + id + ", name=" + name + ", lectures=" + lectures + "]";
	}

}
