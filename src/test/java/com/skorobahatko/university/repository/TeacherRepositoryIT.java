package com.skorobahatko.university.repository;

import com.skorobahatko.university.domain.Teacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.List;
import java.util.Optional;

import static com.skorobahatko.university.util.TestUtils.getTestTeacher;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SqlGroup({
        @Sql("/delete_tables.sql"),
        @Sql("/create_tables.sql"),
        @Sql("/populate_courses.sql"),
        @Sql("/populate_lectures.sql"),
        @Sql("/populate_participants.sql")
})
@Sql(scripts = "/delete_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@DataJpaTest
class TeacherRepositoryIT {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Test
    void injectedRepositoriesAreNotNull() {
        assertThat(teacherRepository).isNotNull();
        assertThat(courseRepository).isNotNull();
    }

    @Test
    void testFindAll() {

        List<Teacher> teachers = teacherRepository.findAll();

        for (Teacher teacher : teachers) {
            assertTrue(teacher instanceof Teacher);
        }

        int expectedTeacherCount = 3;
        int actualTeachersCount = teachers.size();

        assertEquals(expectedTeacherCount, actualTeachersCount);
    }

    @Test
    void testFindById() {
        Teacher teacher = getTestTeacher();
        teacher = teacherRepository.save(teacher);

        int expectedId = teacher.getId();

        Optional<Teacher> teacherOptional = teacherRepository.findById(expectedId);

        assertTrue(teacherOptional.isPresent());

        int actualId = teacherOptional.get().getId();

        assertEquals(expectedId, actualId);
    }

    @Test
    void testSave() {
        int teachersInDatabase = 3;
        Teacher teacher = getTestTeacher();

        teacherRepository.save(teacher);

        int expectedTeachersCount = teachersInDatabase + 1;
        int actualTeachersCount = teacherRepository.findAll().size();

        assertEquals(expectedTeachersCount, actualTeachersCount);
    }

    @Test
    void testDeleteById() {
        Teacher teacher = getTestTeacher();
        teacher = teacherRepository.save(teacher);
        int teacherId = teacher.getId();

        teacherRepository.deleteById(teacherId);

        Optional<Teacher> expected = Optional.empty();
        Optional<Teacher> actual = teacherRepository.findById(teacherId);

        assertEquals(expected, actual);
    }

//    @Test
//    void testAddCourseToTeacherById() {
//        Teacher teacher = getTestTeacher();
//        teacher = teacherRepository.save(teacher);
//
//        System.out.println(teacher.getCourses());
//
//        Course course = getTestCourse();
//        course = courseRepository.save(course);
//
//        assertFalse(teacher.getCourses().contains(course));
//
//        teacherRepository.addCourseToTeacherById(teacher.getId(), course.getId());
//
//        teacher = teacherRepository.findById(teacher.getId()).get();
//
//        System.out.println(teacher.getCourses());
//
//        assertTrue(teacher.getCourses().contains(course));
//    }
//
//    @Test
//    void testDeleteTeachersCourseById() {
//
//    }

}
