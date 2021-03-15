package com.skorobahatko.university.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skorobahatko.university.domain.Course;
import com.skorobahatko.university.service.CourseService;
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
@WebMvcTest(CourseRestController.class)
class CourseRestControllerIT {

    @MockBean
    private CourseService courseService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void testGetAllCourses() throws Exception {
        Course course = new Course(1, "Test Course");
        List<Course> courses = List.of(course);

        when(courseService.getAll()).thenReturn(courses);

        mockMvc.perform(get("/api/courses")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(course.getName())))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(courseService, times(1)).getAll();
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void testAddCourse() throws Exception {
        Course course = new Course("Test Course");

        mockMvc.perform(post("/api/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(course)))
                .andExpect(status().isCreated());

        verify(courseService, times(1)).add(course);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void testGetCourseById() throws Exception {
        Course course = new Course(1, "Test Course");

        when(courseService.getById(1)).thenReturn(course);

        mockMvc.perform(get("/api/courses/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(course)))
                .andReturn();

        verify(courseService, times(1)).getById(1);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void testUpdateCourseById() throws Exception {
        Course course = new Course(1,"Test Course");

        mockMvc.perform(put("/api/courses/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(course)))
                .andExpect(status().isOk());

        verify(courseService, times(1)).update(course);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void testDeleteCourseById() throws Exception {
        mockMvc.perform(delete("/api/courses/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(courseService, times(1)).removeById(1);
        verifyNoMoreInteractions(courseService);
    }
}