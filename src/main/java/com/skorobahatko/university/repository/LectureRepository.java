package com.skorobahatko.university.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skorobahatko.university.domain.Lecture;

@Repository("lectureRepository")
public interface LectureRepository extends JpaRepository<Lecture, Integer> {
	
	List<Lecture> findByCourseId(Integer courseId);
	void deleteByCourseId(Integer courseId);

}
