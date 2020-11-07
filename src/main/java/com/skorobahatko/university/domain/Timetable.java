package com.skorobahatko.university.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Timetable {
	
	private LocalDate startDate;
	private LocalDate endDate;
	private List<TimetableRecord> records;
	
	public Timetable(List<Course> courses, LocalDate startDate, LocalDate endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.records = getRecordsFromCoursesList(courses);
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public List<TimetableRecord> getRecords() {
		return records;
	}
	
	public void addRecord(TimetableRecord record) {
		records.add(record);
	}
	
	public void removeRecord(TimetableRecord record) {
		records.remove(record);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((records == null) ? 0 : records.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
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
		
		Timetable other = (Timetable) obj;
		
		return Objects.equals(startDate, other.startDate) &&
				Objects.equals(endDate, other.endDate) &&
				Objects.equals(records, other.records);
	}

	@Override
	public String toString() {
		return "Timetable [startDate=" + startDate + ", endDate=" + endDate + ", records=" + records + "]";
	}
	
	private List<TimetableRecord> getRecordsFromCoursesList(List<Course> courses) {
		return courses.stream()
				.flatMap(course -> course.getLectures().stream())
				.filter(lecture -> {
					LocalDate lectureDate = lecture.getDate();
					return  (lectureDate.isAfter(startDate) && lectureDate.isBefore(endDate)) || 
							(lectureDate.equals(startDate) || lectureDate.equals(endDate));
				})
				.map(TimetableRecord::of)
				.collect(Collectors.toList());
	}
	
}
