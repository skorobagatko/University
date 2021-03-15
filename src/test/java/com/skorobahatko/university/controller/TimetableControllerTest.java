package com.skorobahatko.university.controller;

import static com.skorobahatko.university.util.TestUtils.getTestParticipant;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import com.skorobahatko.university.domain.Participant;
import com.skorobahatko.university.service.ParticipantService;

@RunWith(MockitoJUnitRunner.class)
class TimetableControllerTest {
	
	private TimetableController timetableController;
	private ParticipantService participantService;
	
	@BeforeEach
	public void init() {
		participantService = Mockito.mock(ParticipantService.class);
		timetableController = new TimetableController(participantService);
	}
	
	@Test
	void allParticipantsAreAddedToModelForTimetableIndexView() {
		Model model = new ExtendedModelMap();
		Participant participant = getTestParticipant();
		List<Participant> participants = List.of(participant);
		
		when(participantService.getAll()).thenReturn(participants);
		
		assertThat(timetableController.getIndexView(model), equalTo("timetables/index"));
		assertThat(model.asMap(), hasEntry("participants", participants));
	}
	
	@Test
	void monthTimetableIsShownForParticipantWithRequestedId() {
		int participantId = 0;
		Model model = new ExtendedModelMap();
		
		Participant participant = getTestParticipant();
		when(participantService.getById(0)).thenReturn(participant);
		
		assertThat(timetableController.getMonthTimetableForParticipant(participantId, model), 
				equalTo("timetables/timetable"));
	}
	
	@Test
	void dayTimetableIsShownForParticipantWithRequestedId() {
		int participantId = 0;
		Model model = new ExtendedModelMap();
		
		Participant participant = getTestParticipant();
		when(participantService.getById(0)).thenReturn(participant);
		
		assertThat(timetableController.getDayTimetableForParticipant(participantId, model), 
				equalTo("timetables/timetable"));
	}

}
