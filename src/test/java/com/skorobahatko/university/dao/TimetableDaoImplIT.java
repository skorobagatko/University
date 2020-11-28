package com.skorobahatko.university.dao;

import static com.skorobahatko.university.dao.util.DaoTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.Optional;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
class TimetableDaoImplIT {
	
	@Inject
	private JdbcTemplate jdbcTemplate;
	
	@Inject	
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
		Timetable timetable = getTestTimetableFor(participant);
		timetableDao.add(timetable);
		
		int expected = timetable.getId();
		int actual = timetableDao.getById(expected).get().getId();
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testAdd() throws SQLException {
		int rowsCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "timetables");
		
		Participant participant = getTestParticipant();
		Timetable timetable = getTestTimetableFor(participant);
		timetableDao.add(timetable);
		
		int expected = rowsCount + 1;
		int actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, "timetables");
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testRemoveById() throws SQLException {
		Participant participant = getTestParticipant();
		Timetable timetable = getTestTimetableFor(participant);
		timetableDao.add(timetable);
		int timetableId = timetable.getId();
		
		timetableDao.removeById(timetableId);
		
		Optional<Timetable> expected = Optional.empty();
		Optional<Timetable> actual = timetableDao.getById(timetableId);
		
		assertEquals(expected, actual);
	}

}
