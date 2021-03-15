package com.skorobahatko.university.service;

import com.skorobahatko.university.domain.Participant;
import com.skorobahatko.university.domain.Student;
import com.skorobahatko.university.repository.StudentRepository;
import com.skorobahatko.university.service.exception.EntityNotFoundException;
import com.skorobahatko.university.service.exception.ValidationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static com.skorobahatko.university.util.TestUtils.getTestStudent;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class StudentServiceImplTest {

    private StudentRepository studentRepository;
    private StudentService studentService;
    
    @BeforeEach
    public void init() {
    	studentRepository = Mockito.mock(StudentRepository.class);
    	studentService = new StudentServiceImpl(studentRepository);
	}

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