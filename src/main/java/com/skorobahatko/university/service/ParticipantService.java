package com.skorobahatko.university.service;

import java.util.List;

import com.skorobahatko.university.domain.Participant;
import com.skorobahatko.university.domain.Student;
import com.skorobahatko.university.domain.Teacher;

public interface ParticipantService extends BaseService<Participant> {
	
	List<Student> getAllStudents();
	List<Teacher> getAllTeachers();
	void addParticipantCourseById(int participantId, int courseId);
	void removeParticipantCourseById(int participantId, int courseId);

}
