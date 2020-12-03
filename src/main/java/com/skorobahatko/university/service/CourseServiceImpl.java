package com.skorobahatko.university.service;

import java.util.List;
import java.util.Optional;

import com.skorobahatko.university.dao.CourseDao;
import com.skorobahatko.university.domain.Course;

public class CourseServiceImpl implements CourseService {
	
	private CourseDao courseDao;
	
	public void setCourseDao(CourseDao courseDao) {
		this.courseDao = courseDao;
	}

	@Override
	public List<Course> getAll() {
		return courseDao.getAll();
	}
	
	@Override
	public List<Course> getAllByParticipantId(int participantId) {
		return courseDao.getAllByParticipantId(participantId);
	}

	@Override
	public Optional<Course> getById(int id) {
		return courseDao.getById(id);
	}

	@Override
	public void add(Course course) {
		courseDao.add(course);
	}

	@Override
	public void removeById(int id) {
		courseDao.removeById(id);
	}

}
