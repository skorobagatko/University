package com.skorobahatko.university.service;

import com.skorobahatko.university.domain.Student;
import com.skorobahatko.university.service.exception.EntityNotFoundException;
import com.skorobahatko.university.service.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;

import static com.skorobahatko.university.util.TestUtils.getTestStudent;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SqlGroup({
        @Sql("/delete_tables.sql"),
        @Sql("/create_tables.sql"),
        @Sql("/populate_courses.sql"),
        @Sql("/populate_participants.sql")
})
@Sql(scripts = "/delete_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

@RunWith(SpringRunner.class)
@SpringBootTest
class StudentServiceImplIT {

    @Autowired
    private StudentService studentService;

    @Test
    void testGetAll() {
        int expected = 5;
        int actual = studentService.getAll().size();

        assertEquals(expected, actual);
    }

    @Test
    void testGetById() {
        Student student = getTestStudent();
        student = studentService.add(student);

        int expectedId = student.getId();
        int actualId = studentService.getById(expectedId).getId();

        assertEquals(expectedId, actualId);
    }

    @Test
    void testGetByIdThrowsExceptionForNonExistStudent() {
        int studentId = Integer.MAX_VALUE;

        assertThrows(EntityNotFoundException.class, () -> studentService.getById(studentId));
    }

    @Test
    void testGetByIdThrowsExceptionForNonValidStudentId() {
        int studentId = -1;

        assertThrows(ValidationException.class, () -> studentService.getById(studentId));
    }

    @Test
    void testAdd() {
        Student student = getTestStudent();

        studentService.add(student);

        int expectedRowsCount = 6;
        int actualRowsCount = studentService.getAll().size();

        assertEquals(expectedRowsCount, actualRowsCount);
    }

    @Test
    void testAddThrowsExceptionForNullStudentArgument() {
        Student student = null;

        assertThrows(ValidationException.class, () -> studentService.add(student));
    }

    @Test
    void testRemoveById() {
        Student student = getTestStudent();
        student = studentService.add(student);
        int studentId = student.getId();

        studentService.removeById(studentId);

        assertThrows(EntityNotFoundException.class, () -> studentService.getById(studentId));
    }

}
