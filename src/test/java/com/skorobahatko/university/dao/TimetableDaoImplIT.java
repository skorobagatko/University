package com.skorobahatko.university.dao;

import static com.skorobahatko.university.util.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;

import com.skorobahatko.university.dao.exception.EntityNotFoundDaoException;
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
class TimetableDaoImplIT {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ParticipantDao participantDao;
	
	@Autowired
	private TimetableDaoImpl timetableDao;

	@Test
	void testGetAll() throws SQLException {
		int expected = JdbcTestUtils.countRowsInTable(jdbcTemplate, "timetables");
		int actual = timetableDao.getAll().size();

		assertEquals(expected, actual);
	}

	@Test
	void testGetById() throws SQLException {
		Participant participant = getTestParticipant();
		participantDao.add(participant);
		Timetable timetable = getTestTimetableForParticipant(participant);
		timetableDao.add(timetable);
		
		int expected = timetable.getId();
		int actual = timetableDao.getById(expected).getId();
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testAdd() throws SQLException {
		int rowsCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "timetables");
		
		Participant participant = getTestParticipant();
		participantDao.add(participant);
		Timetable timetable = getTestTimetableForParticipant(participant);
		
		timetableDao.add(timetable);
		
		int expected = rowsCount + 1;
		int actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, "timetables");
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testRemoveById() throws SQLException {
		Participant participant = getTestParticipant();
		participantDao.add(participant);
		Timetable timetable = getTestTimetableForParticipant(participant);
		timetableDao.add(timetable);
		int timetableId = timetable.getId();
		
		timetableDao.removeById(timetableId);
		
		assertThrows(EntityNotFoundDaoException.class, () -> timetableDao.getById(timetableId));
	}

}
