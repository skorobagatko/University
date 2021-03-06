package com.skorobahatko.university.controller.rest;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skorobahatko.university.domain.Course;
import com.skorobahatko.university.domain.Lecture;
import com.skorobahatko.university.domain.Participant;
import com.skorobahatko.university.domain.Student;
import com.skorobahatko.university.service.ParticipantService;

@RunWith(SpringRunner.class)
@WebMvcTest(TimetableRestController.class)
class TimetableRestControllerTest {
	
	@MockBean
	private ParticipantService participantService;

	@Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

	@Test
	void testGetMonthTimetableByParticipantId() throws Exception {
		Lecture lecture = new Lecture("Test Lecture", 1, LocalDate.now(), LocalTime.of(8, 0), LocalTime.of(9, 0), 100);
		List<Lecture> lectures = List.of(lecture);
		Course course = new Course(1, "Test Course", lectures);
		Participant participant = new Student(1, "Test", "Student");
		participant.addCourse(course);
		
		when(participantService.getById(1)).thenReturn(participant);
		
		mockMvc.perform(get("/api/timetables/month/{participantId}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lectures", hasSize(1)))
                .andExpect(jsonPath("$.lectures[0].date", is(LocalDate.now().toString())))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
		
		verify(participantService, times(1)).getById(1);
        verifyNoMoreInteractions(participantService);
	}

	@Test
	void testGetDayTimetableForParticipant() throws Exception {
		Lecture lecture = new Lecture("Test Lecture", 1, LocalDate.now(), LocalTime.of(8, 0), LocalTime.of(9, 0), 100);
		List<Lecture> lectures = List.of(lecture);
		Course course = new Course(1, "Test Course", lectures);
		Participant participant = new Student(1, "Test", "Student");
		participant.addCourse(course);
		
		when(participantService.getById(1)).thenReturn(participant);
		
		mockMvc.perform(get("/api/timetables/day/{participantId}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lectures", hasSize(1)))
                .andExpect(jsonPath("$.lectures[0].date", is(LocalDate.now().toString())))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
		
		verify(participantService, times(1)).getById(1);
        verifyNoMoreInteractions(participantService);
	}

}
