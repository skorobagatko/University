package com.skorobahatko.university.service;

import static com.skorobahatko.university.util.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.skorobahatko.university.dao.ParticipantDao;
import com.skorobahatko.university.domain.Participant;

class ParticipantServiceImplTest {
	
	private ParticipantServiceImpl participantService;

	@BeforeEach
	void setUp() throws Exception {
		participantService = new ParticipantServiceImpl();
	}

	@Test
	void testGetAll() {
		Participant participant = getTestParticipant();
		List<Participant> expected = List.of(participant);
		
		ParticipantDao participantDao = Mockito.mock(ParticipantDao.class);
		Mockito.when(participantDao.getAll()).thenReturn(expected);
		participantService.setParticipantDao(participantDao);
		
		List<Participant> actual = participantService.getAll();
		
		assertEquals(expected, actual);
	}

	@Test
	void testGetById() {
		int participantId = 1;
		Participant participant = getTestParticipant();
		participant.setId(participantId);
		Optional<Participant> expected = Optional.of(participant);
		
		ParticipantDao participantDao = Mockito.mock(ParticipantDao.class);
		Mockito.when(participantDao.getById(participantId)).thenReturn(expected);
		participantService.setParticipantDao(participantDao);
		
		Optional<Participant> actual = participantService.getById(participantId);
		
		assertEquals(expected, actual);
	}

	@Test
	void testAdd() {
		ParticipantDao participantDao = Mockito.mock(ParticipantDao.class);
		participantService.setParticipantDao(participantDao);
		
		Participant participant = getTestParticipant();
		
		participantService.add(participant);
		
		Mockito.verify(participantDao).add(participant);
	}

	@Test
	void testRemoveById() {
		ParticipantDao participantDao = Mockito.mock(ParticipantDao.class);
		participantService.setParticipantDao(participantDao);
		
		int participantId = 1;
		
		participantService.removeById(participantId);
		
		Mockito.verify(participantDao).removeById(participantId);	
	}

}
