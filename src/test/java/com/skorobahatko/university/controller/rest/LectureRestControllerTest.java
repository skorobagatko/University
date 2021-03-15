package com.skorobahatko.university.controller.rest;

import static com.skorobahatko.university.util.TestUtils.getTestLectureWithCourseId;
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
import com.skorobahatko.university.domain.Lecture;
import com.skorobahatko.university.service.LectureService;

@RunWith(MockitoJUnitRunner.class)
class LectureRestControllerTest {
	
	private LectureRestController lectureRestController;
	private LectureService lectureService;
	private ObjectMapper objectMapper;
	private MockMvc mockMvc;

	@BeforeEach
	public void init() {
		lectureService = Mockito.mock(LectureService.class);
		lectureRestController = new LectureRestController(lectureService);
		mockMvc = MockMvcBuilders.standaloneSetup(lectureRestController).build();
		objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
	}
	
	@Test
	void requestForLecturesListReturnsCorrectJsonResponse() throws Exception {
		Lecture lecture = getTestLectureWithCourseId(1);
		List<Lecture> lectures = List.of(lecture);
		when(lectureService.getAll()).thenReturn(lectures);

		MvcResult mvcResult = mockMvc.perform(get("/api/lectures")).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		
		String expected = objectMapper.writeValueAsString(lectures);
		String actual = mvcResult.getResponse().getContentAsString();
		
		assertEquals(expected, actual);
	}
	
	@Test
	void requestForAddingNewLectureReturnsCorrectStatusCode() throws Exception {
		Lecture lecture = getTestLectureWithCourseId(1);
		String lectureJson = objectMapper.writeValueAsString(lecture);
		when(lectureService.add(lecture)).thenReturn(lecture);
		
		MvcResult mvcResult = mockMvc.perform(post("/api/lectures")
				.contentType(MediaType.APPLICATION_JSON)
				.content(lectureJson))
				.andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(201, status);
		
		String actual = mvcResult.getResponse().getContentAsString();
		assertEquals(lectureJson, actual);
	}
	
	@Test
	void requestForGettingLectureByIdReturnsCorrectJsonResponse() throws Exception {
		Lecture lecture = getTestLectureWithCourseId(1);
		when(lectureService.getById(0)).thenReturn(lecture);
		
		MvcResult mvcResult = mockMvc.perform(get("/api/lectures/{id}", 0)).andReturn();
        
        int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		
		String expected = objectMapper.writeValueAsString(lecture);
		String actual = mvcResult.getResponse().getContentAsString();
		assertEquals(expected, actual);
	}
	
	@Test
	void requestForUpdatingLectureReturnsCorrectStatusCode() throws Exception {
		Lecture lecture = getTestLectureWithCourseId(1);
		
		MvcResult mvcResult = mockMvc.perform(put("/api/lectures/{id}", 0)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(lecture)))
				.andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}
	
	@Test
	void requestForDeletingLecturesReturnsCorrectStatusCode() throws Exception {
		MvcResult mvcResult = mockMvc.perform(delete("/api/lectures/{id}", 1)).andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}

}
