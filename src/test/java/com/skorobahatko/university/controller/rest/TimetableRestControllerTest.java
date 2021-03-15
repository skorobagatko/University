package com.skorobahatko.university.controller.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.skorobahatko.university.domain.Course;
import com.skorobahatko.university.domain.Lecture;
import com.skorobahatko.university.domain.Participant;
import com.skorobahatko.university.domain.Student;
import com.skorobahatko.university.domain.Timetable;
import com.skorobahatko.university.service.ParticipantService;

@RunWith(MockitoJUnitRunner.class)
class TimetableRestControllerTest {
	
	private TimetableRestController timetableRestController;
	private ParticipantService participantService;
	private ObjectMapper objectMapper;
	private MockMvc mockMvc;
	
	@BeforeEach
	public void init() {
		participantService = Mockito.mock(ParticipantService.class);
		timetableRestController = new TimetableRestController(participantService);
		mockMvc = MockMvcBuilders.standaloneSetup(timetableRestController).build();
		objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
	}
	
	@Test
	void requestForMonthTimetableReturnsCorrectTimetableJsonResponse() throws Exception {
		Lecture lecture = new Lecture("Test Lecture", 1, LocalDate.now(), LocalTime.of(8, 0), LocalTime.of(9, 0), 100);
		List<Lecture> lectures = List.of(lecture);
		Course course = new Course(1, "Test Course", lectures);
		Participant participant = new Student(1, "Test", "Student");
		participant.addCourse(course);
		
		when(participantService.getById(1)).thenReturn(participant);
		
		MvcResult mvcResult = mockMvc.perform(get("/api/timetables/month/{participantId}", 1))
				.andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		
		Timetable timetable = Timetable.getMonthTimetable(participant);
		String expected = objectMapper.writeValueAsString(timetable);
		String actual = mvcResult.getResponse().getContentAsString();
		assertEquals(expected, actual);
	}
	
	@Test
	void requestForDayTimetableReturnsCorrectTimetableJsonResponse() throws Exception {
		Lecture lecture = new Lecture("Test Lecture", 1, LocalDate.now(), LocalTime.of(8, 0), LocalTime.of(9, 0), 100);
		List<Lecture> lectures = List.of(lecture);
		Course course = new Course(1, "Test Course", lectures);
		Participant participant = new Student(1, "Test", "Student");
		participant.addCourse(course);
		
		when(participantService.getById(1)).thenReturn(participant);
		
		MvcResult mvcResult = mockMvc.perform(get("/api/timetables/day/{participantId}", 1))
				.andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		
		Timetable timetable = Timetable.getDayTimetable(participant);
		String expected = objectMapper.writeValueAsString(timetable);
		String actual = mvcResult.getResponse().getContentAsString();
		assertEquals(expected, actual);
	}

}
