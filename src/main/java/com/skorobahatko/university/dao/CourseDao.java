package com.skorobahatko.university.dao;

import java.util.List;

import com.skorobahatko.university.domain.Course;

public interface CourseDao extends BaseDao<Course> {
	
	List<Course> getAllByParticipantId(int participantId);

}
