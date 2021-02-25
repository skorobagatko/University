package com.skorobahatko.university.controller;

import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import static com.skorobahatko.university.util.TestUtils.*;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.skorobahatko.university.domain.Participant;
import com.skorobahatko.university.domain.Student;
import com.skorobahatko.university.domain.Timetable;
import com.skorobahatko.university.service.ParticipantService;

@RunWith(SpringRunner.class)
@WebMvcTest(TimetableController.class)
class TimetableControllerTest {
	
	@MockBean
	private ParticipantService participantService;
	
	@Autowired
	MockMvc mockMvc;

	@Test
	void testIndex() throws Exception {
		List<Participant> participants = List.of(getTestParticipant());
		
		when(participantService.getAll()).thenReturn(participants);
		
		mockMvc.perform(get("/timetables"))
				.andExpect(status().isOk())
				.andExpect(view().name("timetables/index"))
				.andExpect(model().attribute("participants", equalTo(participants)));
		
		verify(participantService, times(1)).getAll();
		verifyNoMoreInteractions(participantService);
	}

	@Test
	void testGetMonthTimetableForParticipant() throws Exception {
		Student student = new Student(1, "John", "Johnson");
		Timetable timetable = Timetable.getMonthTimetable(student);
		
		when(participantService.getById(1)).thenReturn(student);
		
		mockMvc.perform(get("/timetables/month")
				.param("participantId", "1"))
				.andExpect(status().isOk())
				.andExpect(view().name("timetables/timetable"))
				.andExpect(model().attribute("timetable", equalTo(timetable)));
		
		verify(participantService, times(1)).getById(1);
		verifyNoMoreInteractions(participantService);
	}
	
	@Test
	void testGetDayTimetableForParticipant() throws Exception {
		Student student = new Student(1, "John", "Johnson");
		Timetable timetable = Timetable.getDayTimetable(student);
		
		when(participantService.getById(1)).thenReturn(student);
		
		mockMvc.perform(get("/timetables/day")
				.param("participantId", "1"))
				.andExpect(status().isOk())
				.andExpect(view().name("timetables/timetable"))
				.andExpect(model().attribute("timetable", equalTo(timetable)));
		
		verify(participantService, times(1)).getById(1);
		verifyNoMoreInteractions(participantService);
	}

}
