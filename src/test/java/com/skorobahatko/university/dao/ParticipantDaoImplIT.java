package com.skorobahatko.university.dao;

import static com.skorobahatko.university.util.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import com.skorobahatko.university.dao.exception.EntityNotFoundDaoException;
import com.skorobahatko.university.domain.Participant;
import com.skorobahatko.university.domain.Student;
import com.skorobahatko.university.domain.Teacher;

@SqlGroup({ 
	@Sql("/delete_tables.sql"), 
	@Sql("/create_tables.sql"), 
	@Sql("/populate_courses.sql"),
	@Sql("/populate_participants.sql")
})
@Sql(scripts = "/delete_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class ParticipantDaoImplIT {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	ParticipantDao participantDao;

	@Test
	void testGetAll() {
		int expected = JdbcTestUtils.countRowsInTable(jdbcTemplate, "participants");
		int actual = participantDao.getAll().size();

		assertEquals(expected, actual);
	}
	
	@Test
	void testGetAllStudents() {
		List<Student> expected = participantDao.getAll().stream()
				.filter(p -> p.getClass() == Student.class)
				.map(p -> (Student) p)
				.collect(Collectors.toList());
		
		List<Student> actual = participantDao.getAllStudents();
		
		assertEquals(expected.size(), actual.size());
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetAllTeachers() {
		List<Teacher> expected = participantDao.getAll().stream()
				.filter(p -> p.getClass() == Teacher.class)
				.map(p -> (Teacher) p)
				.collect(Collectors.toList());
		
		List<Teacher> actual = participantDao.getAllTeachers();
		
		assertEquals(expected, actual);
	}

	@Test
	void testGetById() {
		Participant participant = getTestParticipant();
		participantDao.add(participant);
		
		int expectedId = participant.getId();
		int actualId = participantDao.getById(expectedId).getId();
		
		assertEquals(expectedId, actualId);
	}
	
	@Test
	void testAdd() {
		int rowsCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "participants");
		Participant participant = getTestParticipant();
		
		participantDao.add(participant);
		
		int expected = rowsCount + 1;
		int actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, "participants");
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testRemoveById() {
		Participant participant = getTestParticipant();
		participantDao.add(participant);
		int participantId = participant.getId();
		
		participantDao.removeById(participantId);
		
		assertThrows(EntityNotFoundDaoException.class, () -> participantDao.getById(participantId));
	}

}
