package com.skorobahatko.university.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Timetable {
	
	private Participant participant;
	private LocalDate startDate;
	private LocalDate endDate;
	private List<Lecture> lectures;
	
	public static Timetable getDayTimetable(Participant participant) {
		return getDayTimetable(participant, LocalDate.now());
	}
	
	public static Timetable getDayTimetable(Participant participant, LocalDate date) {
		return new Timetable(participant, date, date);
	}
	
	public static Timetable getMonthTimetable(Participant participant) {
		return getMonthTimetable(participant, getFirstDayOfCurrentMonthDate());
	}
	
	public static Timetable getMonthTimetable(Participant participant, LocalDate date) {
		return new Timetable(participant, date, date.plusMonths(1));
	}
	
	private Timetable(Participant participant, LocalDate startDate, LocalDate endDate) {
		setParticipant(participant);
		setStartDate(startDate);
		setEndDate(endDate);
		setLectures(getFilteredLecturesFromCourses(participant.getCourses()));
	}
	
	public Participant getParticipant() {
		return participant;
	}
	
	public LocalDate getStartDate() {
		return startDate;
	}
	
	public LocalDate getEndDate() {
		return endDate;
	}
	
	public List<Lecture> getLectures() {
		return lectures;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
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
		
		return Objects.equals(participant, other.participant) &&
				Objects.equals(startDate, other.startDate) &&
				Objects.equals(endDate, other.endDate) &&
				Objects.equals(lectures, other.lectures);
	}

	@Override
	public String toString() {
		return "Timetable [participant=" + participant + ", startDate=" 
				+ startDate + ", endDate=" + endDate + ", lectures=" + lectures + "]";
	}
	
	private void setParticipant(Participant participant) {
		validateParticipant(participant);
		
		this.participant = participant;
	}
	
	private void setStartDate(LocalDate startDate) {
		validateStartDate(startDate);
		
		this.startDate = startDate;
	}
	
	private void setEndDate(LocalDate endDate) {
		validateEndDate(endDate);
		
		this.endDate = endDate;
	}
	
	private void setLectures(List<Lecture> lectures) {
		this.lectures = lectures;
	}
	
	private static LocalDate getFirstDayOfCurrentMonthDate() {
		LocalDate now = LocalDate.now();
		int currentMonth = now.getMonthValue();
		int currentYear = now.getYear();
		
		return LocalDate.of(currentYear, currentMonth, 1);
	}

	private List<Lecture> getFilteredLecturesFromCourses(List<Course> courses) {
		return courses
				.stream()
				.flatMap(course -> course.getLectures().stream())
				.filter(lecture -> {
					LocalDate lectureDate = lecture.getDate();
					
					return  lectureDate.isEqual(startDate) || 
							lectureDate.isEqual(endDate) || 
							(lectureDate.isAfter(startDate) && lectureDate.isBefore(endDate));
				})
				.sorted((l1, l2) -> l1.getStartTime().compareTo(l2.getStartTime()))
				.sorted((l1, l2) -> l1.getDate().compareTo(l2.getDate()))
				.collect(Collectors.toList());
	}
	
	private void validateParticipant(Participant participant) {
		if (participant == null) {
			String message = String.format("The participant must not be null. "
					+ "Actual participant was %s", participant);
			throw new IllegalArgumentException(message);
		}
	}
	
	private void validateDate(LocalDate date) {
		if (date == null) {
			String message = String.format("The date must not be null. "
					+ "Actual date was %s", date);
			throw new IllegalArgumentException(message);
		}
	}
	
	private void validateStartDate(LocalDate startDate) {
		validateDate(startDate);
		
		if (this.endDate != null && startDate.isAfter(this.endDate)) {
			String message = String.format(
					"The timetable start date must be less than its end date. "
					+ "Actual start date was %s, end date was %s", startDate, this.endDate);
			throw new IllegalArgumentException(message);
		}
	}
	
	private void validateEndDate(LocalDate endDate) {
		validateDate(endDate);
		
		if (this.startDate != null && endDate.isBefore(this.startDate)) {
			String message = String.format(
					"The timetable end date must be greater than its start date. "
					+ "Actual start date was %s, end date was %s", this.startDate, endDate);
			throw new IllegalArgumentException(message);
		}
	}
	
}
