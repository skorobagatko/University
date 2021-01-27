package com.skorobahatko.university.service;

import static com.skorobahatko.university.util.TestUtils.getTestParticipant;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;

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
@ExtendWith(SpringExtension.class)
@ContextConfiguration("file:src/test/resources/springTestContext.xml")
class ParticipantServiceImplIT {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ParticipantService participantService;

	@Test
	void testGetAll() {
		int expected = JdbcTestUtils.countRowsInTable(jdbcTemplate, "participants");
		int actual = participantService.getAll().size();

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
		int rowsCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "participants");
		Participant participant = getTestParticipant();
		
		participantService.add(participant);
		
		int expectedRowsCount = rowsCount + 1;
		int actualRowsCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "participants");
		
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
