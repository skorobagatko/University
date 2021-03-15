package com.skorobahatko.university.controller.rest;

import static com.skorobahatko.university.util.TestUtils.getTestCourse;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skorobahatko.university.domain.Course;
import com.skorobahatko.university.service.CourseService;

@RunWith(MockitoJUnitRunner.class)
class CourseRestControllerTest {

	private CourseRestController courseRestController;
	private CourseService courseService;
	private ObjectMapper objectMapper;
	private MockMvc mockMvc;

	@BeforeEach
	public void init() {
		courseService = Mockito.mock(CourseService.class);
		courseRestController = new CourseRestController(courseService);
		mockMvc = MockMvcBuilders.standaloneSetup(courseRestController).build();
		objectMapper = new ObjectMapper();
	}

	@Test
	void requestForCoursesListReturnsCorrectJsonResponse() throws Exception {
		Course course = getTestCourse();
		List<Course> courses = List.of(course);
		when(courseService.getAll()).thenReturn(courses);

		MvcResult mvcResult = mockMvc.perform(get("/api/courses")).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		
		String expected = objectMapper.writeValueAsString(courses);
		String actual = mvcResult.getResponse().getContentAsString();
		assertEquals(expected, actual);
	}
	
	@Test
	void requestForAddingNewCourseReturnsCorrectStatusCode() throws Exception {
		Course course = getTestCourse();
		
		MvcResult mvcResult = mockMvc.perform(post("/api/courses")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(course)))
				.andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(201, status);
	}
	
	@Test
	void requestForGettingCourseByIdReturnsCorrectJsonResponse() throws Exception {
		Course course = new Course(1, "Test Course");
        when(courseService.getById(1)).thenReturn(course);

        MvcResult mvcResult = mockMvc.perform(get("/api/courses/{id}", 1)).andReturn();
        
        int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		
		String expected = objectMapper.writeValueAsString(course);
		String actual = mvcResult.getResponse().getContentAsString();
		assertEquals(expected, actual);
	}
	
	@Test
	void requestForUpdatingCourseReturnsCorrectStatusCode() throws Exception {
		Course course = new Course(1, "Test Course");
		
		MvcResult mvcResult = mockMvc.perform(put("/api/courses/{id}", 1)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(course)))
				.andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}
	
	@Test
	void requestForDeletingCourseReturnsCorrectStatusCode() throws Exception {
		MvcResult mvcResult = mockMvc.perform(delete("/api/courses/{id}", 1)).andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}

}
