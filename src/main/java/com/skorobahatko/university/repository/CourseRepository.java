package com.skorobahatko.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skorobahatko.university.domain.Course;

@Repository("courseRepository")
public interface CourseRepository extends JpaRepository<Course, Integer> {

}
