package com.skorobahatko.university.service;

import java.util.List;

import com.skorobahatko.university.domain.Lecture;

public interface LectureService extends BaseService<Lecture> {
	
	List<Lecture> getByCourseId(int courseId);
	void addAll(List<Lecture> lectures);
	void removeByCourseId(int courseId);

}
