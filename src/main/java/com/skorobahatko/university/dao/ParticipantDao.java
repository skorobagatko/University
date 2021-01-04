package com.skorobahatko.university.dao;

import java.util.List;

import com.skorobahatko.university.domain.Participant;
import com.skorobahatko.university.domain.Student;
import com.skorobahatko.university.domain.Teacher;

public interface ParticipantDao extends BaseDao<Participant> {
	
	List<Student> getAllStudents();
	List<Teacher> getAllTeachers();
	void addParticipantCourseById(int participantId, int courseId);
	void removeParticipantCourseById(int participantId, int courseId);

}
