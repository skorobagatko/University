package com.skorobahatko.university.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Lecture {
	
	private String courseId;
	private String name;
	private LocalDate date;
	private LocalTime startTime;
	private LocalTime endTime;
	private int roomNumber;
	
	Lecture(String name, Course course, LocalDate date, LocalTime startTime, LocalTime endTime, int roomNumber) {
		this.courseId = course.getId();
		this.name = name;
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
		this.roomNumber = roomNumber;
	}
	
	public String getCourseId() {
		return courseId;
	}
	
	public String getName() {
		return name;
	}

	public LocalDate getDate() {
		return date;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public int getRoomNumber() {
		return roomNumber;
	}
	
	void setDate(LocalDate date) {
		this.date = date;
	}
	
	void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}
	
	void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((courseId == null) ? 0 : courseId.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + roomNumber;
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
		
		return Objects.equals(name, other.name) &&
				Objects.equals(courseId, other.courseId) &&
				Objects.equals(date, other.date) &&
				Objects.equals(startTime, other.startTime) &&
				Objects.equals(endTime, other.endTime) &&
				roomNumber == other.roomNumber;
	}

	@Override
	public String toString() {
		return "Lecture [courseId=" + courseId + ", name=" + name + ", date=" + date + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", roomNumber=" + roomNumber + "]";
	}

}
