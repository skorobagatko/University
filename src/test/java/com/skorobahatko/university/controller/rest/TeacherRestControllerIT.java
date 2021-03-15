package com.skorobahatko.university.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skorobahatko.university.domain.Teacher;
import com.skorobahatko.university.service.TeacherService;
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
@WebMvcTest(TeacherRestController.class)
class TeacherRestControllerIT {

    @MockBean
    private TeacherService teacherService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void testGetAllTeachers() throws Exception {
        Teacher teacher = new Teacher("Test", "Teacher");
        List<Teacher> teachers = List.of(teacher);

        when(teacherService.getAll()).thenReturn(teachers);

        mockMvc.perform(get("/api/teachers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is(teacher.getFirstName())))
                .andExpect(jsonPath("$[0].lastName", is(teacher.getLastName())))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(teacherService, times(1)).getAll();
        verifyNoMoreInteractions(teacherService);
    }

    @Test
    void testAddTeacher() throws Exception {
        Teacher teacher = new Teacher("Test", "Teacher");

        mockMvc.perform(post("/api/teachers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(teacher)))
                .andExpect(status().isCreated());

        verify(teacherService, times(1)).add(teacher);
        verifyNoMoreInteractions(teacherService);
    }

    @Test
    void testGetTeacherById() throws Exception {
        Teacher teacher = new Teacher(1, "Test", "Teacher");

        when(teacherService.getById(1)).thenReturn(teacher);

        mockMvc.perform(get("/api/teachers/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(teacher)))
                .andReturn();

        verify(teacherService, times(1)).getById(1);
        verifyNoMoreInteractions(teacherService);
    }

    @Test
    void testUpdateTeacherById() throws Exception {
        Teacher teacher = new Teacher(1, "Test", "Teacher");

        mockMvc.perform(put("/api/teachers/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(teacher)))
                .andExpect(status().isOk());

        verify(teacherService, times(1)).update(teacher);
        verifyNoMoreInteractions(teacherService);
    }

    @Test
    void testDeleteTeacherById() throws Exception {
        mockMvc.perform(delete("/api/teachers/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(teacherService, times(1)).removeById(1);
        verifyNoMoreInteractions(teacherService);
    }

    @Test
    void testAddCourseToTeacher() throws Exception {
        Teacher teacher = new Teacher(1, "Test", "Teacher");

        when(teacherService.getById(1)).thenReturn(teacher);

        mockMvc.perform(post("/api/teachers/{teacherId}/course/{courseId}", 1, 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(teacher)));

        verify(teacherService, times(1)).addCourseToTeacherById(1, 1);
        verify(teacherService, times(1)).getById(1);
        verifyNoMoreInteractions(teacherService);
    }

    @Test
    void testDeleteCourseFromTeacher() throws Exception {
        Teacher teacher = new Teacher(1, "Test", "Teacher");

        when(teacherService.getById(1)).thenReturn(teacher);

        mockMvc.perform(delete("/api/teachers/{teacherId}/course/{courseId}", 1, 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(teacher)));

        verify(teacherService, times(1)).deleteTeachersCourseById(1, 1);
        verify(teacherService, times(1)).getById(1);
        verifyNoMoreInteractions(teacherService);
    }
    
}