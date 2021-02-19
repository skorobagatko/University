package com.skorobahatko.university.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.skorobahatko.university.domain.Participant;
import com.skorobahatko.university.domain.Student;
import com.skorobahatko.university.domain.Teacher;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Integer> {
	
	@Query("select s from Student s")
	List<Student> findAllStudents();
	
	@Query("select t from Teacher t")
	List<Teacher> findAllTeachers();
	
	@Modifying
	@Query(value = "INSERT INTO participants_courses (participant_id, course_id) VALUES (?1, ?2)", nativeQuery = true)
	void addParticipantCourseById(int participantId, int courseId);
	
	@Modifying
	@Query(value = "DELETE FROM participants_courses WHERE participant_id = ?1 AND course_id = ?2", nativeQuery = true)
	void deleteParticipantCourseById(int participantId, int courseId);

}
