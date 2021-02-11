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

import org.hibernate.annotations.*;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "lectures")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Lecture {
	
	@Id
	@Column(name = "lecture_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "course_id")
	private int courseId;
	
	@Column(name = "lecture_name")
	private String name;
	
	@Column(name = "lecture_date")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate date;
	
	@Column(name = "lecture_start_time")
	@DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
	private LocalTime startTime;
	
	@Column(name = "lecture_end_time")
	@DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
	private LocalTime endTime;
	
	@Column(name = "lecture_room_number")
	private int roomNumber;
	
	public Lecture() {}
	
	public Lecture(String name, LocalDate date, LocalTime startTime, LocalTime endTime, int roomNumber) {
		this(0, name, 0, date, startTime, endTime, roomNumber);
	}
	
	public Lecture(String name, int courseId, LocalDate date, LocalTime startTime, LocalTime endTime, int roomNumber) {
		this(0, name, courseId, date, startTime, endTime, roomNumber);
	}
	
	public Lecture(int id, String name, int courseId, LocalDate date, LocalTime startTime, LocalTime endTime, int roomNumber) {
		this.id = id;
		this.courseId = courseId;
		this.name = name;
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
		this.roomNumber = roomNumber;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getCourseId() {
		return courseId;
	}
	
	public void setCourseId(int courseId) {
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
