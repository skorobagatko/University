package com.skorobahatko.university.repository;

import com.skorobahatko.university.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("studentRepository")
public interface StudentRepository extends JpaRepository<Student, Integer> {

    @Modifying
    @Query(value = "INSERT INTO participants_courses (participant_id, course_id) VALUES (?1, ?2)", nativeQuery = true)
    void addCourseToStudentById(int studentId, int courseId);

    @Modifying
    @Query(value = "DELETE FROM participants_courses WHERE participant_id = ?1 AND course_id = ?2", nativeQuery = true)
    void deleteStudentsCourseById(int studentId, int courseId);

    @Query(value = "SELECT course_id FROM participants_courses WHERE participant_id = ?1", nativeQuery = true)
    List<Integer> getStudentCoursesById(int studentId);

}
