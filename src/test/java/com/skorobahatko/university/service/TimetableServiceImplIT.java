package com.skorobahatko.university.service;

import static com.skorobahatko.university.util.TestUtils.getTestParticipant;
import static com.skorobahatko.university.util.TestUtils.getTestTimetableForParticipant;
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
import com.skorobahatko.university.domain.Timetable;

@SqlGroup({ 
	@Sql("/delete_tables.sql"), 
	@Sql("/create_tables.sql"), 
	@Sql("/populate_courses.sql"),
	@Sql("/populate_lectures.sql"),
	@Sql("/populate_participants.sql"),
	@Sql("/populate_timetables.sql"),
})
@Sql(scripts = "/delete_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@ContextConfiguration("/applicationContext.xml")
class TimetableServiceImplIT {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ParticipantService participantService;
	
	@Autowired
	private TimetableService timetableService;

	@Test
	void testGetAll() {
		int expectedRowsCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "timetables");
		int actualRowsCount = timetableService.getAll().size();

		assertEquals(expectedRowsCount, actualRowsCount);
	}

	@Test
	void testGetById() {
		Participant participant = getTestParticipant();
		participantService.add(participant);
		Timetable timetable = getTestTimetableForParticipant(participant);
		timetableService.add(timetable);
		
		int expectedId = timetable.getId();
		int actualId = timetableService.getById(expectedId).get().getId();
		
		assertEquals(expectedId, actualId);
	}

	@Test
	void testAdd() {
		int rowsCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "timetables");
		
		Participant participant = getTestParticipant();
		participantService.add(participant);
		Timetable timetable = getTestTimetableForParticipant(participant);
		
		timetableService.add(timetable);
		
		int expectedRowsCount = rowsCount + 1;
		int actualRowsCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "timetables");
		
		assertEquals(expectedRowsCount, actualRowsCount);
	}

	@Test
	void testRemoveById() {
		Participant participant = getTestParticipant();
		participantService.add(participant);
		Timetable timetable = getTestTimetableForParticipant(participant);
		timetableService.add(timetable);
		int timetableId = timetable.getId();
		
		timetableService.removeById(timetableId);
		
		Optional<Timetable> expected = Optional.empty();
		Optional<Timetable> actual = timetableService.getById(timetableId);
		
		assertEquals(expected, actual);
	}

}
