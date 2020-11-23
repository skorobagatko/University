package com.skorobahatko.university.dao;

import static com.skorobahatko.university.dao.util.DatabaseTestUtils.*;
import static com.skorobahatko.university.dao.util.DaoTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.skorobahatko.university.domain.Participant;
import com.skorobahatko.university.domain.Timetable;

class TimetableDaoTest {
	
	private TimetableDao timetableDao;

	@BeforeEach
	void setUp() throws Exception {
		initializeTestDatabase();
		
		timetableDao = new TimetableDao(getTestDataSource());
	}
	
	@Test
	void testGetAll() throws SQLException {
		Participant participant = getTestParticipant();
		Timetable timetable = getTestTimetableFor(participant);
		
		insertParticipantToTheTestDatabase(participant);
		insertTimetableToTheTestDatabase(timetable);
		
		List<Timetable> expected = List.of(timetable);
		
		List<Timetable> actual = timetableDao.getAll();
		
		assertEquals(expected, actual);
	}

	@Test
	void testGetById() throws SQLException {
		Participant participant = getTestParticipant();
		Timetable timetable = getTestTimetableFor(participant);
		
		insertParticipantToTheTestDatabase(participant);
		insertTimetableToTheTestDatabase(timetable);
		
		Optional<Timetable> expected = Optional.of(timetable);
		
		Optional<Timetable> actual = timetableDao.getById(timetable.getId());
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testAdd() throws SQLException {
		Participant participant = getTestParticipant();
		Timetable timetable = getTestTimetableFor(participant);
		
		insertParticipantToTheTestDatabase(participant);

		timetableDao.add(timetable);
		
		Optional<Timetable> expected = Optional.of(timetable);
		
		Optional<Timetable> actual = timetableDao.getById(timetable.getId());
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testRemoveById() throws SQLException {
		Participant participant = getTestParticipant();
		Timetable timetable = getTestTimetableFor(participant);
		
		insertParticipantToTheTestDatabase(participant);
		insertTimetableToTheTestDatabase(timetable);
		
		timetableDao.removeById(timetable.getId());
		
		Optional<Timetable> expected = Optional.empty();
		
		Optional<Timetable> actual = timetableDao.getById(timetable.getId());
		
		assertEquals(expected, actual);
	}
	
	private Timetable getTestTimetableFor(Participant participant) {
		int timetableId = 1;
		LocalDate startDate = LocalDate.of(2020, 12, 1);
		LocalDate endDate = LocalDate.of(2020, 12, 3);
		return new Timetable(timetableId, participant, startDate, endDate);
	}
	

}
