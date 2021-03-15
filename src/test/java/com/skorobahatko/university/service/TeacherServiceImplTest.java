package com.skorobahatko.university.service;

import com.skorobahatko.university.domain.Participant;
import com.skorobahatko.university.domain.Teacher;
import com.skorobahatko.university.repository.TeacherRepository;
import com.skorobahatko.university.service.exception.EntityNotFoundException;
import com.skorobahatko.university.service.exception.ValidationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static com.skorobahatko.university.util.TestUtils.getTestTeacher;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(MockitoJUnitRunner.class)
class TeacherServiceImplTest {

    private TeacherRepository teacherRepository;
    private TeacherService teacherService;
    
    @BeforeEach
    public void init() {
    	teacherRepository = Mockito.mock(TeacherRepository.class);
    	teacherService = new TeacherServiceImpl(teacherRepository);
	}

    @Test
    void testGetAll() {
        Teacher teacher = getTestTeacher();
        List<Teacher> expected = List.of(teacher);

        Mockito.when(teacherRepository.findAll()).thenReturn(expected);

        List<Teacher> actual = teacherService.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void testGetById() {
        int teacherId = 1;
        Teacher expected = getTestTeacher();
        expected.setId(teacherId);

        Mockito.when(teacherRepository.findById(teacherId))
                .thenReturn(Optional.of(expected));

        Participant actual = teacherService.getById(teacherId);

        assertEquals(expected, actual);
    }

    @Test
    void testGetByIdThrowsExceptionForNonExistParticipant() {
        int teacherId = Integer.MAX_VALUE;

        Mockito.when(teacherRepository.findById(teacherId))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class,
                () -> teacherService.getById(teacherId));
    }

    @Test
    void testGetByIdThrowsExceptionForNonValidParticipantId() {
        int teacherId = -1;

        assertThrows(ValidationException.class,
                () -> teacherService.getById(teacherId));
    }

    @Test
    void testAdd() {
        Teacher teacher = getTestTeacher();

        teacherService.add(teacher);

        Mockito.verify(teacherRepository).save(teacher);
    }

    @Test
    void testAddThrowsExceptionForNullParticipantArgument() {
        Teacher teacher = null;

        assertThrows(ValidationException.class, () -> teacherService.add(teacher));
    }

    @Test
    void testUpdate() {
        Teacher teacher = getTestTeacher();

        teacherService.update(teacher);

        Mockito.verify(teacherRepository).save(teacher);
    }

    @Test
    void testRemoveById() {
        int teacherId = 1;

        teacherService.removeById(teacherId);

        Mockito.verify(teacherRepository).deleteById(teacherId);
    }

//    @Test
//    void addCourseToTeacherById() {
//    }
//
//    @Test
//    void deleteTeachersCourseById() {
//    }
}