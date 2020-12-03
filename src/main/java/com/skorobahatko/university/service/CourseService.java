package com.skorobahatko.university.service;

import java.util.List;

import com.skorobahatko.university.domain.Course;

public interface CourseService extends BaseService<Course> {
	
	List<Course> getAllByParticipantId(int participantId);

}
