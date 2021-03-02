package com.skorobahatko.university.service;

import com.skorobahatko.university.domain.Teacher;

public interface TeacherService extends BaseService<Teacher> {

    void addCourseToTeacherById(int teacherId, int courseId);
    void deleteTeachersCourseById(int teacherId, int courseId);

}
