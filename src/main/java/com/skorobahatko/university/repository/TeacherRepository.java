package com.skorobahatko.university.repository;

import com.skorobahatko.university.domain.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("teacherRepository")
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    @Modifying
    @Query(value = "INSERT INTO participants_courses (participant_id, course_id) VALUES (?1, ?2)", nativeQuery = true)
    void addCourseToTeacherById(int teacherId, int courseId);

    @Modifying
    @Query(value = "DELETE FROM participants_courses WHERE participant_id = ?1 AND course_id = ?2", nativeQuery = true)
    void deleteTeachersCourseById(int teacherId, int courseId);

}
