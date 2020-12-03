package com.skorobahatko.university.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Timetable {
	
	private int id;
	private Participant participant;
	private LocalDate startDate;
	private LocalDate endDate;
	private List<Lecture> lectures;
	
	public static Timetable getDayTimetable(Participant participant, LocalDate date) {
		return getTimetable(participant, date, date);
	}
	
	public static Timetable getMonthTimetable(Participant participant, LocalDate date) {
		return getTimetable(participant, date, date.plusMonths(1));
	}
	
	private static Timetable getTimetable(Participant participant, LocalDate startDate, LocalDate endDate) {
		return new Timetable(0, participant, startDate, endDate);
	}
	
	public Timetable(Participant participant, LocalDate startDate, LocalDate endDate) {
		this(0, participant, startDate, endDate);
	}
	
	public Timetable(int id, Participant participant, LocalDate startDate, LocalDate endDate) {
		this.id = id;
		this.participant = participant;
		this.startDate = startDate;
		this.endDate = endDate;
		this.lectures = getFilteredLecturesFromCourses(participant.getCourses());
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Participant getParticipant() {
		return participant;
	}
	
	public void setParticipant(Participant participant) {
		this.participant = participant;
	}

	public LocalDate getStartDate() {
		return startDate;
	}
	
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}
	
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
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
		result = prime * result + ((participant == null) ? 0 : participant.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
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
		
		Timetable other = (Timetable) obj;
		
		return id == other.id &&
				Objects.equals(participant, other.participant) &&
				Objects.equals(startDate, other.startDate) &&
				Objects.equals(endDate, other.endDate) &&
				Objects.equals(lectures, other.lectures);
	}

	@Override
	public String toString() {
		return "Timetable [id=" + id + ", participant=" + participant + ", startDate=" + startDate + ", endDate="
				+ endDate + ", lectures=" + lectures + "]";
	}

	private List<Lecture> getFilteredLecturesFromCourses(List<Course> courses) {
		return courses
				.stream()
				.flatMap(course -> course.getLectures().stream())
				.filter(lecture -> {
					LocalDate lectureDate = lecture.getDate();
					return  (lectureDate.isAfter(startDate) && lectureDate.isBefore(endDate)) || 
							(lectureDate.equals(startDate) || lectureDate.equals(endDate));
				})
				.collect(Collectors.toList());
				
				
	}
	
}
