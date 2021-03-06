package com.skorobahatko.university.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.hibernate.annotations.*;
import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "lectures")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@ApiModel(description = "University lecture")
public class Lecture {
	
	@Id
	@Column(name = "lecture_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty("The unique ID of the lecture")
	private Integer id;
	
	@Column(name = "course_id")
	@ApiModelProperty("The unique ID of the course")
	private Integer courseId;
	
	@NotBlank(message = "The lecture name must not be empty")
	@Size(min=2, max=100, message = "The name length must be between 2 and 100")
	@Column(name = "lecture_name")
	@ApiModelProperty("The name of the lecture")
	private String name;
	
	@NotNull(message = "The lecture date must not be empty")
	@Column(name = "lecture_date")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@ApiModelProperty("The date of the lecture")
	private LocalDate date;
	
	@NotNull(message = "The lecture start time must not be empty")
	@Column(name = "lecture_start_time")
	@DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
	@ApiModelProperty("The start time of the lecture")
	private LocalTime startTime;
	
	@NotNull(message = "The lecture end time must not be empty")
	@Column(name = "lecture_end_time")
	@DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
	@ApiModelProperty("The end time of the lecture")
	private LocalTime endTime;
	
	@Positive(message = "The room number must be positive integer")
	@Column(name = "lecture_room_number")
	@ApiModelProperty("The room number of the lecture")
	private int roomNumber;
	
	public Lecture() {}
	
	public Lecture(String name, int courseId, LocalDate date, LocalTime startTime, LocalTime endTime, int roomNumber) {
		this(0, name, courseId, date, startTime, endTime, roomNumber);
	}
	
	public Lecture(Integer id, String name, int courseId, LocalDate date, LocalTime startTime, LocalTime endTime, int roomNumber) {
		setId(id);
		setCourseId(courseId);
		setName(name);
		setDate(date);
		setStartTime(startTime);
		setEndTime(endTime);
		setRoomNumber(roomNumber);
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getCourseId() {
		return courseId;
	}
	
	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getStartTime() {
		return startTime;
	}
	
	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}
	
	public int getRoomNumber() {
		return roomNumber;
	}
	
	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + courseId;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + roomNumber;
		result = prime * result + id;
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
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
		
		Lecture other = (Lecture) obj;
		
		return id == other.id &&
				roomNumber == other.roomNumber &&
				courseId == other.courseId &&
				Objects.equals(name, other.name) &&
				Objects.equals(date, other.date) &&
				Objects.equals(startTime, other.startTime) &&
				Objects.equals(endTime, other.endTime);
	}

	@Override
	public String toString() {
		return "Lecture [id=" + id + ", courseId=" + courseId + ", name=" + name + ", date=" + date + ", startTime="
				+ startTime + ", endTime=" + endTime + ", roomNumber=" + roomNumber + "]";
	}

}
