package com.skorobahatko.university.repository;

import static com.skorobahatko.university.util.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import com.skorobahatko.university.domain.Participant;

@SqlGroup({ 
	@Sql("/delete_tables.sql"), 
	@Sql("/create_tables.sql"), 
	@Sql("/populate_courses.sql"),
	@Sql("/populate_lectures.sql"),
	@Sql("/populate_participants.sql")
})
@DataJpaTest
class ParticipantRepositoryIT {

	@Autowired
	private ParticipantRepository participantRepository;
	
	@Test
	void injectedRepositoriesAreNotNull() {
		assertThat(participantRepository).isNotNull();
	}

	@Test
	void testFindAll() {
		int expected = 8;
		int actual = participantRepository.findAll().size();

		assertEquals(expected, actual);
	}

	@Test
	void testFindById() {
		Participant participant = getTestParticipant();
		participant = participantRepository.save(participant);

		int expectedId = participant.getId();

		Optional<Participant> participantOptional = participantRepository.findById(expectedId);

		assertTrue(participantOptional.isPresent());

		int actualId = participantOptional.get().getId();

		assertEquals(expectedId, actualId);
	}

	@Test
	void testSave() {
		int participantsInDatabase = 8;
		Participant participant = getTestParticipant();

		participantRepository.save(participant);

		int expected = participantsInDatabase + 1;
		int actual = participantRepository.findAll().size();

		assertEquals(expected, actual);
	}

	@Test
	void testDeleteById() {
		Participant participant = getTestParticipant();
		participant = participantRepository.save(participant);
		int participantId = participant.getId();

		participantRepository.deleteById(participantId);

		Optional<Participant> expected = Optional.empty();
		Optional<Participant> actual = participantRepository.findById(participantId);

		assertEquals(expected, actual);
	}
	
}
