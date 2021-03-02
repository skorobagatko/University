package com.skorobahatko.university.service;

import com.skorobahatko.university.domain.Participant;
import com.skorobahatko.university.domain.Student;
import com.skorobahatko.university.repository.StudentRepository;
import com.skorobahatko.university.service.exception.EntityNotFoundException;
import com.skorobahatko.university.service.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static com.skorobahatko.university.util.TestUtils.getTestStudent;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class StudentServiceImplTest {

    @MockBean
    private StudentRepository studentRepository;

    @Autowired
    private StudentService studentService;

    @Test
    void testGetAll() {
        Student student = getTestStudent();
        List<Student> expected = List.of(student);

        Mockito.when(studentRepository.findAll()).thenReturn(expected);

        List<Student> actual = studentService.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void testGetById() {
        int studentId = 1;
        Student expected = getTestStudent();
        expected.setId(studentId);

        Mockito.when(studentRepository.findById(studentId))
                .thenReturn(Optional.of(expected));

        Participant actual = studentService.getById(studentId);

        assertEquals(expected, actual);
    }

    @Test
    void testGetByIdThrowsExceptionForNonExistParticipant() {
        int studentId = Integer.MAX_VALUE;

        Mockito.when(studentRepository.findById(studentId))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class,
                () -> studentService.getById(studentId));
    }

    @Test
    void testGetByIdThrowsExceptionForNonValidParticipantId() {
        int studentId = -1;

        assertThrows(ValidationException.class,
                () -> studentService.getById(studentId));
    }

    @Test
    void testAdd() {
        Student student = getTestStudent();

        studentService.add(student);

        Mockito.verify(studentRepository).save(student);
    }

    @Test
    void testAddThrowsExceptionForNullParticipantArgument() {
        Student student = null;

        assertThrows(ValidationException.class, () -> studentService.add(student));
    }

    @Test
    void testUpdate() {
        Student student = getTestStudent();

        studentService.update(student);

        Mockito.verify(studentRepository).save(student);
    }

    @Test
    void testRemoveById() {
        int studentId = 1;

        studentService.removeById(studentId);

        Mockito.verify(studentRepository).deleteById(studentId);
    }

//    @Test
//    void addCourseToStudentById() {
//    }
//
//    @Test
//    void deleteStudentsCourseById() {
//    }

}