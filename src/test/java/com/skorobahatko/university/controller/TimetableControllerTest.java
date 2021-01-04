package com.skorobahatko.university.controller;

import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import static com.skorobahatko.university.util.TestUtils.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.skorobahatko.university.domain.Participant;
import com.skorobahatko.university.domain.Student;
import com.skorobahatko.university.domain.Timetable;
import com.skorobahatko.university.service.ParticipantService;
import com.skorobahatko.university.service.TimetableService;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:springTestContext.xml", "file:src/main/webapp/WEB-INF/servletContext.xml"})
@WebAppConfiguration
class TimetableControllerTest {
	
	MockMvc mockMvc;
	
	@Autowired
    private WebApplicationContext webApplicationContext;
	
	@Autowired
	private TimetableService timetableService;
	
	@Autowired
	private ParticipantService participantService;

	@BeforeEach
	void setUp() throws Exception {
		reset(timetableService);
		reset(participantService);
		
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

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
	void testGetTimetableForParticipant() throws Exception {
		Student student = new Student(1, "John", "Johnson");
		Timetable timetable = getTestTimetableForParticipant(student);
		
		when(timetableService.getByParticipantId(1)).thenReturn(timetable);
		
		mockMvc.perform(get("/timetables/participant")
				.param("participantIdSelect", "1"))
				.andExpect(status().isOk())
				.andExpect(view().name("timetables/timetable"))
				.andExpect(model().attribute("timetable", equalTo(timetable)));
		
		verify(timetableService, times(1)).getByParticipantId(1);
		verifyNoMoreInteractions(timetableService);
	}

}
