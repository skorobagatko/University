package com.skorobahatko.university.dao;

import java.util.List;

import com.skorobahatko.university.domain.Lecture;

public interface LectureDao extends BaseDao<Lecture> {
	
	List<Lecture> getByCourseId(int courseId);
	void addAll(List<Lecture> lectures);
	void removeByCourseId(int courseId);

}
