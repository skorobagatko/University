package com.skorobahatko.university.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "participants")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public abstract class Participant {

	@Id
	@Column(name = "participant_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty("The unique ID of the participant")
	private Integer id;

	@NotBlank(message = "The first name must not be empty")
	@Size(min=2, max=100, message = "The first name length must be between 2 and 100")
	@Column(name = "first_name")
	@ApiModelProperty("The first name of the participant")
	private String firstName;

	@NotBlank(message = "The last name must not be empty")
	@Size(min=2, max=100, message = "The last name length must be between 2 and 100")
	@Column(name = "last_name")
	@ApiModelProperty("The last name of the participant")
	private String lastName;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "participants_courses", 
			joinColumns = @JoinColumn(name = "participant_id"), 
			inverseJoinColumns = @JoinColumn(name = "course_id"))
	@ApiModelProperty("The participant's course list")
	private List<Course> courses;

	Participant() {
		setCourses(new ArrayList<>());
	}

	Participant(Integer id, String firstName, String lastName, List<Course> courses) {
		setId(id);
		setFirstName(firstName);
		setLastName(lastName);
		setCourses(new ArrayList<>(courses));
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

		return id == other.id 
				&& Objects.equals(firstName, other.firstName) 
				&& Objects.equals(lastName, other.lastName)
				&& Objects.equals(courses, other.courses);
	}

	@Override
	public String toString() {
		return "Participant [id=" + id + ", firstName=" + firstName + ", "
				+ "lastName=" + lastName + ", courses=" + courses + "]";
	}

}
