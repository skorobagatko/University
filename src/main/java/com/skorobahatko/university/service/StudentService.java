package com.skorobahatko.university.service;

import com.skorobahatko.university.domain.Student;

public interface StudentService extends BaseService<Student> {

    void addCourseToStudentById(int studentId, int courseId);
    void deleteStudentsCourseById(int studentId, int courseId);

}
