package com.skorobahatko.university.service;

import static com.skorobahatko.university.util.TestUtils.getTestParticipant;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

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

@SqlGroup({ 
	@Sql("/delete_tables.sql"), 
	@Sql("/create_tables.sql"), 
	@Sql("/populate_courses.sql"),
	@Sql("/populate_participants.sql")
})
@Sql(scripts = "/delete_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@ContextConfiguration("/applicationContext.xml")
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
		int actualId = participantService.getById(expectedId).get().getId();
		
		assertEquals(expectedId, actualId);
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
	void testRemoveById() {
		Participant participant = getTestParticipant();
		participantService.add(participant);
		int participantId = participant.getId();
		
		participantService.removeById(participantId);
		
		Optional<Participant> expected = Optional.empty();
		Optional<Participant> actual = participantService.getById(participantId);
		
		assertEquals(expected, actual);
	}

}
