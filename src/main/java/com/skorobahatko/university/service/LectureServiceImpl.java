package com.skorobahatko.university.service;

import java.util.List;
import java.util.Optional;

import com.skorobahatko.university.dao.LectureDao;
import com.skorobahatko.university.domain.Lecture;

public class LectureServiceImpl implements LectureService {

	private LectureDao lectureDao;
	
	public void setLectureDao(LectureDao lectureDao) {
		this.lectureDao = lectureDao;
	}
	
	@Override
	public List<Lecture> getAll() {
		return lectureDao.getAll();
	}

	@Override
	public Optional<Lecture> getById(int id) {
		return lectureDao.getById(id);
	}
	
	@Override
	public List<Lecture> getByCourseId(int courseId) {
		return lectureDao.getByCourseId(courseId);
	}
	
	@Override
	public void addAll(List<Lecture> lectures) {
		lectureDao.addAll(lectures);
	}

	@Override
	public void add(Lecture lecture) {
		lectureDao.add(lecture);
	}

	@Override
	public void removeById(int id) {
		lectureDao.removeById(id);
	}

}
