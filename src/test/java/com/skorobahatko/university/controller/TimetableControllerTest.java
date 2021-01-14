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

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:springTestContext.xml", "file:src/main/webapp/WEB-INF/servletContext.xml"})
@WebAppConfiguration
class TimetableControllerTest {
	
	MockMvc mockMvc;
	
	@Autowired
    private WebApplicationContext webApplicationContext;
	
	@Autowired
	private ParticipantService participantService;

	@BeforeEach
	void setUp() throws Exception {
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
		Timetable timetable = Timetable.getMonthTimetable(student);
		
		when(participantService.getById(1)).thenReturn(student);
		
		mockMvc.perform(get("/timetables/participant")
				.param("participantId", "1"))
				.andExpect(status().isOk())
				.andExpect(view().name("timetables/timetable"))
				.andExpect(model().attribute("timetable", equalTo(timetable)));
		
		verify(participantService, times(1)).getById(1);
		verifyNoMoreInteractions(participantService);
	}

}
