package com.skorobahatko.university.controller.rest;

import static com.skorobahatko.university.util.TestUtils.getTestStudent;
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
import com.skorobahatko.university.domain.Student;
import com.skorobahatko.university.service.StudentService;

@RunWith(MockitoJUnitRunner.class)
class StudentRestControllerTest {
	
	private StudentRestController studentRestController;
	private StudentService studentService;
	private ObjectMapper objectMapper;
	private MockMvc mockMvc;
	
	@BeforeEach
	public void init() {
		studentService = Mockito.mock(StudentService.class);
		studentRestController = new StudentRestController(studentService);
		mockMvc = MockMvcBuilders.standaloneSetup(studentRestController).build();
		objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
	}
	
	@Test
	void requestForStudentsListReturnsCorrectJsonResponse() throws Exception {
		Student student = getTestStudent();
		List<Student> students = List.of(student);
		when(studentService.getAll()).thenReturn(students);

		MvcResult mvcResult = mockMvc.perform(get("/api/students")).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		
		String expected = objectMapper.writeValueAsString(students);
		String actual = mvcResult.getResponse().getContentAsString();
		assertEquals(expected, actual);
	}
	
	@Test
	void requestForAddingNewStudentReturnsCorrectStatusCode() throws Exception {
		Student student = getTestStudent();
		
		MvcResult mvcResult = mockMvc.perform(post("/api/students")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(student)))
				.andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(201, status);
	}
	
	@Test
	void requestForGettingCourseByIdReturnsCorrectJsonResponse() throws Exception {
		Student student = getTestStudent();
        when(studentService.getById(0)).thenReturn(student);

        MvcResult mvcResult = mockMvc.perform(get("/api/students/{id}", 0)).andReturn();
        
        int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		
		String expected = objectMapper.writeValueAsString(student);
		String actual = mvcResult.getResponse().getContentAsString();
		assertEquals(expected, actual);
	}
	
	@Test
	void requestForUpdatingStudentReturnsCorrectStatusCode() throws Exception {
		Student student = getTestStudent();
		
		MvcResult mvcResult = mockMvc.perform(put("/api/students/{id}", 0)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(student)))
				.andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}
	
	@Test
	void requestForDeletingStudentReturnsCorrectStatusCode() throws Exception {
		MvcResult mvcResult = mockMvc.perform(delete("/api/students/{id}", 1)).andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}
	
	@Test
	void requestForAddingStudentCourseReturnsCorrectJsonResponse() throws Exception {
		Student student = new Student(1, "Test", "Student");

        when(studentService.getById(1)).thenReturn(student);
        
        MvcResult mvcResult = mockMvc.perform(post("/api/students/{studentId}/course/{courseId}", 1, 1)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(student)))
				.andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		
		String expected = objectMapper.writeValueAsString(student);
		String actual = mvcResult.getResponse().getContentAsString();
		assertEquals(expected, actual);
	}
	
	@Test
	void requestForDeletingStudentCourseReturnsCorrectJsonResponse() throws Exception {
		Student student = new Student(1, "Test", "Student");

        when(studentService.getById(1)).thenReturn(student);
        
        MvcResult mvcResult = mockMvc.perform(delete("/api/students/{studentId}/course/{courseId}", 1, 1)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(student)))
				.andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		
		String expected = objectMapper.writeValueAsString(student);
		String actual = mvcResult.getResponse().getContentAsString();
		assertEquals(expected, actual);
	}

}
