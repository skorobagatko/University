package com.skorobahatko.university.controller.rest;

import static com.skorobahatko.university.util.TestUtils.getTestTeacher;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

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
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.skorobahatko.university.domain.Teacher;
import com.skorobahatko.university.service.TeacherService;

@RunWith(MockitoJUnitRunner.class)
class TeacherRestControllerTest {
	
	private TeacherRestController teacherRestController;
	private TeacherService teacherService;
	private ObjectMapper objectMapper;
	private MockMvc mockMvc;
	
	@BeforeEach
	public void init() {
		teacherService = Mockito.mock(TeacherService.class);
		teacherRestController = new TeacherRestController(teacherService);
		mockMvc = MockMvcBuilders.standaloneSetup(teacherRestController).build();
		objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
	}
	
	@Test
	void requestForTeachersListReturnsCorrectJsonResponse() throws Exception {
		Teacher teacher = getTestTeacher();
		List<Teacher> teachers = List.of(teacher);
		when(teacherService.getAll()).thenReturn(teachers);

		MvcResult mvcResult = mockMvc.perform(get("/api/teachers")).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		
		String expected = objectMapper.writeValueAsString(teachers);
		String actual = mvcResult.getResponse().getContentAsString();
		assertEquals(expected, actual);
	}
	
	@Test
	void requestForAddingNewTeacherReturnsCorrectStatusCode() throws Exception {
		Teacher teacher = getTestTeacher();
		
		MvcResult mvcResult = mockMvc.perform(post("/api/teachers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(teacher)))
				.andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(201, status);
	}
	
	@Test
	void requestForGettingCourseByIdReturnsCorrectJsonResponse() throws Exception {
		Teacher teacher = getTestTeacher();
        when(teacherService.getById(0)).thenReturn(teacher);

        MvcResult mvcResult = mockMvc.perform(get("/api/teachers/{id}", 0)).andReturn();
        
        int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		
		String expected = objectMapper.writeValueAsString(teacher);
		String actual = mvcResult.getResponse().getContentAsString();
		assertEquals(expected, actual);
	}
	
	@Test
	void requestForUpdatingTeacherReturnsCorrectStatusCode() throws Exception {
		Teacher teacher = getTestTeacher();
		
		MvcResult mvcResult = mockMvc.perform(put("/api/teachers/{id}", 0)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(teacher)))
				.andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}
	
	@Test
	void requestForDeletingTeacherReturnsCorrectStatusCode() throws Exception {
		MvcResult mvcResult = mockMvc.perform(delete("/api/teachers/{id}", 1)).andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}
	
	@Test
	void requestForAddingTeacherCourseReturnsCorrectJsonResponse() throws Exception {
		Teacher teacher = new Teacher(1, "Test", "Teacher");

        when(teacherService.getById(1)).thenReturn(teacher);
        
        MvcResult mvcResult = mockMvc.perform(post("/api/teachers/{teacherId}/course/{courseId}", 1, 1)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(teacher)))
				.andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		
		String expected = objectMapper.writeValueAsString(teacher);
		String actual = mvcResult.getResponse().getContentAsString();
		assertEquals(expected, actual);
	}
	
	@Test
	void requestForDeletingTeacherCourseReturnsCorrectJsonResponse() throws Exception {
		Teacher teacher = new Teacher(1, "Test", "Teacher");

        when(teacherService.getById(1)).thenReturn(teacher);
        
        MvcResult mvcResult = mockMvc.perform(delete("/api/teachers/{teacherId}/course/{courseId}", 1, 1)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(teacher)))
				.andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		
		String expected = objectMapper.writeValueAsString(teacher);
		String actual = mvcResult.getResponse().getContentAsString();
		assertEquals(expected, actual);
	}

}
