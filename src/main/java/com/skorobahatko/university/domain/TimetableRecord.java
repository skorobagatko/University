package com.skorobahatko.university.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class TimetableRecord {
	
	private LocalDate date;
	private String lectureName;
	private LocalTime startTime;
	private LocalTime endTime;
	private int roomNumber;
	
	public static TimetableRecord of(Lecture lecture) {
		return new TimetableRecord(lecture.getDate(), 
				lecture.getName(),
				lecture.getStartTime(),
				lecture.getEndTime(),
				lecture.getRoomNumber());
	}
	
	private TimetableRecord(LocalDate date, String lectureName, LocalTime startTime, LocalTime endTime, int roomNumber) {
		this.date = date;
		this.lectureName = lectureName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.roomNumber = roomNumber;
	}

	public LocalDate getDate() {
		return date;
	}

	public String getLectureName() {
		return lectureName;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + ((lectureName == null) ? 0 : lectureName.hashCode());
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
		
		TimetableRecord other = (TimetableRecord) obj;
		
		return Objects.equals(date, other.date) &&
				Objects.equals(lectureName, other.lectureName) &&
				Objects.equals(startTime, other.startTime) &&
				Objects.equals(endTime, other.endTime) &&
				roomNumber == other.roomNumber;
	}

	@Override
	public String toString() {
		return "TimetableRecord [date=" + date + ", lectureName=" + lectureName + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", roomNumber=" + roomNumber + "]";
	}
	
}
