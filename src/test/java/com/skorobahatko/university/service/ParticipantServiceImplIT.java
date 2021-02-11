package com.skorobahatko.university.service;

import static com.skorobahatko.university.util.TestUtils.getTestParticipant;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;

import com.skorobahatko.university.domain.Participant;
import com.skorobahatko.university.service.exception.EntityNotFoundServiceException;
import com.skorobahatko.university.service.exception.ValidationException;

@SqlGroup({ 
	@Sql("/delete_tables.sql"), 
	@Sql("/create_tables.sql"), 
	@Sql("/populate_courses.sql"),
	@Sql("/populate_participants.sql")
})
@Sql(scripts = "/delete_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

@RunWith(SpringRunner.class)
@SpringBootTest
class ParticipantServiceImplIT {
	
	@Autowired
	private ParticipantService participantService;

	@Test
	void testGetAll() {
		int expected = 8;
		int actual = participantService.getAll().size();

		assertEquals(expected, actual);
	}
	
	@Test
	void testGetAllStudents() {
		int expected = 5;
		int actual = participantService.getAllStudents().size();

		assertEquals(expected, actual);
	}
	
	@Test
	void testGetAllTeachers() {
		int expected = 3;
		int actual = participantService.getAllTeachers().size();

		assertEquals(expected, actual);
	}

	@Test
	void testGetById() {
		Participant participant = getTestParticipant();
		participantService.add(participant);
		
		int expectedId = participant.getId();
		int actualId = participantService.getById(expectedId).getId();
		
		assertEquals(expectedId, actualId);
	}
	
	@Test
	void testGetByIdThrowsExceptionForNonExistParticipant() {
		int participantId = Integer.MAX_VALUE;
		
		assertThrows(EntityNotFoundServiceException.class, () -> participantService.getById(participantId));
	}
	
	@Test
	void testGetByIdThrowsExceptionForNonValidParticipantId() {
		int participantId = -1;

		assertThrows(ValidationException.class, () -> participantService.getById(participantId));
	}

	@Test
	void testAdd() {
		Participant participant = getTestParticipant();
		
		participantService.add(participant);
		
		int expectedRowsCount = 9;
		int actualRowsCount = participantService.getAll().size();
		
		assertEquals(expectedRowsCount, actualRowsCount);
	}
	
	@Test
	void testAddThrowsExceptionForNullParticipantArgument() {
		Participant participant = null;
		
		assertThrows(ValidationException.class, () -> participantService.add(participant));
	}

	@Test
	void testRemoveById() {
		Participant participant = getTestParticipant();
		participantService.add(participant);
		int participantId = participant.getId();
		
		participantService.removeById(participantId);
		
		assertThrows(EntityNotFoundServiceException.class, () -> participantService.getById(participantId));
	}

}
