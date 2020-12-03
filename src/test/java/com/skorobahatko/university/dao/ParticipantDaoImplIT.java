package com.skorobahatko.university.dao;

import static com.skorobahatko.university.util.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
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
class ParticipantDaoImplIT {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	ParticipantDaoImpl participantDao;

	@Test
	void testGetAll() throws SQLException {
		int expected = JdbcTestUtils.countRowsInTable(jdbcTemplate, "participants");
		int actual = participantDao.getAll().size();

		assertEquals(expected, actual);
	}

	@Test
	void testGetById() throws SQLException {
		Participant participant = getTestParticipant();
		participantDao.add(participant);
		
		int expectedId = participant.getId();
		int actualId = participantDao.getById(expectedId).get().getId();
		
		assertEquals(expectedId, actualId);
	}
	
	@Test
	void testAdd() throws SQLException {
		int rowsCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "participants");
		Participant participant = getTestParticipant();
		
		participantDao.add(participant);
		
		int expected = rowsCount + 1;
		int actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, "participants");
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testRemoveById() throws SQLException {
		Participant participant = getTestParticipant();
		participantDao.add(participant);
		int participantId = participant.getId();
		
		participantDao.removeById(participantId);
		
		Optional<Participant> expected = Optional.empty();
		Optional<Participant> actual = participantDao.getById(participantId);
		
		assertEquals(expected, actual);
	}

}
