package com.skorobahatko.university.repository;

import static com.skorobahatko.university.util.TestUtils.getTestStudent;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.skorobahatko.university.domain.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.List;
import java.util.Optional;

@SqlGroup({
        @Sql("/delete_tables.sql"),
        @Sql("/create_tables.sql"),
        @Sql("/populate_courses.sql"),
        @Sql("/populate_lectures.sql"),
        @Sql("/populate_participants.sql")
})
@DataJpaTest
class StudentRepositoryIT {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Test
    void injectedRepositoriesAreNotNull() {
        assertThat(studentRepository).isNotNull();
        assertThat(courseRepository).isNotNull();
    }

    @Test
    void testFindAll() {

        List<Student> students = studentRepository.findAll();

        for (Student student : students) {
            assertTrue(student instanceof Student);
        }

        int expectedStudentCount = 5;
        int actualStudentsCount = students.size();

        assertEquals(expectedStudentCount, actualStudentsCount);
    }

    @Test
    void testFindById() {
        Student student = getTestStudent();
        student = studentRepository.save(student);

        int expectedId = student.getId();

        Optional<Student> studentOptional = studentRepository.findById(expectedId);

        assertTrue(studentOptional.isPresent());

        int actualId = studentOptional.get().getId();

        assertEquals(expectedId, actualId);
    }

    @Test
    void testSave() {
        int studentsInDatabase = 5;
        Student student = getTestStudent();

        studentRepository.save(student);

        int expectedStudentsCount = studentsInDatabase + 1;
        int actualStudentsCount = studentRepository.findAll().size();

        assertEquals(expectedStudentsCount, actualStudentsCount);
    }

    @Test
    void testDeleteById() {
        Student student = getTestStudent();
        student = studentRepository.save(student);
        int studentId = student.getId();

        studentRepository.deleteById(studentId);

        Optional<Student> expected = Optional.empty();
        Optional<Student> actual = studentRepository.findById(studentId);

        assertEquals(expected, actual);
    }

//    @Test
//    void testAddCourseToStudentById() {
//        Student student = getTestStudent();
//        student = studentRepository.save(student);
//
//        Course course = getTestCourse();
//        course = courseRepository.saveAndFlush(course);
//
//        assertFalse(student.getCourses().contains(course));
//
//        System.out.println("Student courses = " + studentRepository.getStudentCoursesById(student.getId()));
//
//        studentRepository.addCourseToStudentById(student.getId(), course.getId());
//
//        System.out.println("Student courses = " + studentRepository.getStudentCoursesById(student.getId()));
//
//        studentRepository.flush();
//
//        student = studentRepository.findById(student.getId()).get();
//
//        System.out.println(student.getCourses());
//
//        assertTrue(student.getCourses().contains(course));
//    }
//
//    @Test
//    void testDeleteStudentsCourseById() {
//
//    }

}
