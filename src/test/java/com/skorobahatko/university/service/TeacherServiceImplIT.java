package com.skorobahatko.university.service;

import com.skorobahatko.university.domain.Teacher;
import com.skorobahatko.university.service.exception.EntityNotFoundException;
import com.skorobahatko.university.service.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;

import static com.skorobahatko.university.util.TestUtils.getTestTeacher;
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
class TeacherServiceImplIT {

    @Autowired
    private TeacherService teacherService;

    @Test
    void testGetAll() {
        int expected = 3;
        int actual = teacherService.getAll().size();

        assertEquals(expected, actual);
    }

    @Test
    void testGetById() {
        Teacher teacher = getTestTeacher();
        teacher = teacherService.add(teacher);

        int expectedId = teacher.getId();
        int actualId = teacherService.getById(expectedId).getId();

        assertEquals(expectedId, actualId);
    }

    @Test
    void testGetByIdThrowsExceptionForNonExistTeacher() {
        int teacherId = Integer.MAX_VALUE;

        assertThrows(EntityNotFoundException.class, () -> teacherService.getById(teacherId));
    }

    @Test
    void testGetByIdThrowsExceptionForNonValidTeacherId() {
        int teacherId = -1;

        assertThrows(ValidationException.class, () -> teacherService.getById(teacherId));
    }

    @Test
    void testAdd() {
        Teacher teacher = getTestTeacher();

        teacherService.add(teacher);

        int expectedRowsCount = 4;
        int actualRowsCount = teacherService.getAll().size();

        assertEquals(expectedRowsCount, actualRowsCount);
    }

    @Test
    void testAddThrowsExceptionForNullTeacherArgument() {
        Teacher teacher = null;

        assertThrows(ValidationException.class, () -> teacherService.add(teacher));
    }

    @Test
    void testRemoveById() {
        Teacher teacher = getTestTeacher();
        teacher = teacherService.add(teacher);
        int teacherId = teacher.getId();

        teacherService.removeById(teacherId);

        assertThrows(EntityNotFoundException.class, () -> teacherService.getById(teacherId));
    }
    
}
