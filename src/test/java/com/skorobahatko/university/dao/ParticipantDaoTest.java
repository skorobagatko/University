package com.skorobahatko.university.dao;

import static com.skorobahatko.university.dao.util.DatabaseTestUtils.*;
import static com.skorobahatko.university.dao.util.DaoTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.skorobahatko.university.domain.Participant;

class ParticipantDaoTest {
	
	ParticipantDao participantDao;

	@BeforeEach
	void setUp() throws Exception {
		initializeTestDatabase();
		
		participantDao = new ParticipantDao(getTestDataSource());
	}
	
	@Test
	void testGetAll() throws SQLException {
		Participant participant = getTestParticipant();
		insertParticipantToTheTestDatabase(participant);
		
		List<Participant> expected = List.of(participant);
		
		List<Participant> actual = participantDao.getAll();
		
		assertEquals(expected, actual);
	}

	@Test
	void testGetById() throws SQLException {
		Participant participant = getTestParticipant();
		int participantId = participant.getId();
		insertParticipantToTheTestDatabase(participant);
		
		Optional<Participant> expected = Optional.of(participant);
		
		Optional<Participant> actual = participantDao.getById(participantId);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testAdd() throws SQLException {
		Participant participant = getTestParticipant();
		int participantId = participant.getId();
		int roleId = 1;
		String roleName = "Student";
		insertParticipantRole(roleId, roleName);
		
		Optional<Participant> expected = Optional.of(participant);

		participantDao.add(participant);
		
		Optional<Participant> actual = participantDao.getById(participantId);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testRemoveById() throws SQLException {
		Participant participant = getTestParticipant();
		int participantId = participant.getId();
		insertParticipantToTheTestDatabase(participant);
		
		participantDao.removeById(participantId);
		
		Optional<Participant> expected = Optional.empty();
		
		Optional<Participant> actual = participantDao.getById(participantId);
		
		assertEquals(expected, actual);
	}

}
