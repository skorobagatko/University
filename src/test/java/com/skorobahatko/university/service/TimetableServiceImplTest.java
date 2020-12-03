package com.skorobahatko.university.service;

import static com.skorobahatko.university.util.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.skorobahatko.university.dao.TimetableDao;
import com.skorobahatko.university.domain.Participant;
import com.skorobahatko.university.domain.Timetable;

class TimetableServiceImplTest {
	
	private TimetableServiceImpl timetableService;

	@BeforeEach
	void setUp() throws Exception {
		timetableService = new TimetableServiceImpl();
	}

	@Test
	void testGetAll() {
		Participant participant = getTestParticipant();
		Timetable timetable = getTestTimetableForParticipant(participant);
		List<Timetable> expected = List.of(timetable);
		
		TimetableDao timetableDao = Mockito.mock(TimetableDao.class);
		Mockito.when(timetableDao.getAll()).thenReturn(expected);
		timetableService.setTimetableDao(timetableDao);
		
		List<Timetable> actual = timetableService.getAll();
		
		assertEquals(expected, actual);
	}

	@Test
	void testGetById() {
		Participant participant = getTestParticipant();
		Timetable timetable = getTestTimetableForParticipant(participant);
		int timetableId = 1;
		timetable.setId(timetableId);
		Optional<Timetable> expected = Optional.of(timetable);
		
		TimetableDao timetableDao = Mockito.mock(TimetableDao.class);
		Mockito.when(timetableDao.getById(timetableId)).thenReturn(expected);
		timetableService.setTimetableDao(timetableDao);
		
		Optional<Timetable> actual = timetableService.getById(timetableId);
		
		assertEquals(expected, actual);
	}

	@Test
	void testAdd() {
		Participant participant = getTestParticipant();
		Timetable timetable = getTestTimetableForParticipant(participant);
		
		TimetableDao timetableDao = Mockito.mock(TimetableDao.class);
		timetableService.setTimetableDao(timetableDao);
		
		timetableService.add(timetable);
		
		Mockito.verify(timetableDao).add(timetable);
	}

	@Test
	void testRemoveById() {
		int timetableId = 1;
		
		TimetableDao timetableDao = Mockito.mock(TimetableDao.class);
		timetableService.setTimetableDao(timetableDao);
		
		timetableService.removeById(timetableId);
		
		Mockito.verify(timetableDao).removeById(timetableId);
	}

}
