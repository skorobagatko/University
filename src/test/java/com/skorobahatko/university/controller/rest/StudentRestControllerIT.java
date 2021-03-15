package com.skorobahatko.university.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skorobahatko.university.domain.Student;
import com.skorobahatko.university.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(StudentRestController.class)
class StudentRestControllerIT {

    @MockBean
    private StudentService studentService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void testGetAllStudents() throws Exception {
        Student student = new Student("Test", "Student");
        List<Student> students = List.of(student);

        when(studentService.getAll()).thenReturn(students);

        mockMvc.perform(get("/api/students")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is(student.getFirstName())))
                .andExpect(jsonPath("$[0].lastName", is(student.getLastName())))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(studentService, times(1)).getAll();
        verifyNoMoreInteractions(studentService);
    }

    @Test
    void testAddStudent() throws Exception {
        Student student = new Student("Test", "Student");

        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isCreated());

        verify(studentService, times(1)).add(student);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    void testGetStudentById() throws Exception {
        Student student = new Student(1, "Test", "Student");

        when(studentService.getById(1)).thenReturn(student);

        mockMvc.perform(get("/api/students/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(student)));

        verify(studentService, times(1)).getById(1);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    void testUpdateStudentById() throws Exception {
        Student student = new Student(1, "Test", "Student");

        mockMvc.perform(put("/api/students/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk());

        verify(studentService, times(1)).update(student);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    void testDeleteStudentById() throws Exception {
        mockMvc.perform(delete("/api/students/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(studentService, times(1)).removeById(1);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    void testAddCourseToStudent() throws Exception {
        Student student = new Student(1, "Test", "Student");

        when(studentService.getById(1)).thenReturn(student);

        mockMvc.perform(post("/api/students/{studentId}/course/{courseId}", 1, 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(student)));

        verify(studentService, times(1)).addCourseToStudentById(1, 1);
        verify(studentService, times(1)).getById(1);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    void testDeleteCourseFromStudent() throws Exception {
        Student student = new Student(1, "Test", "Student");

        when(studentService.getById(1)).thenReturn(student);

        mockMvc.perform(delete("/api/students/{studentId}/course/{courseId}", 1, 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(student)));

        verify(studentService, times(1)).deleteStudentsCourseById(1, 1);
        verify(studentService, times(1)).getById(1);
        verifyNoMoreInteractions(studentService);
    }

}