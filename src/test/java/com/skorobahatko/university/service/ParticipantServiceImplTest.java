package com.skorobahatko.university.service;

import static com.skorobahatko.university.util.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.skorobahatko.university.domain.Participant;
import com.skorobahatko.university.repository.ParticipantRepository;
import com.skorobahatko.university.service.exception.EntityNotFoundServiceException;
import com.skorobahatko.university.service.exception.ValidationException;

@RunWith(SpringRunner.class)
@SpringBootTest
class ParticipantServiceImplTest {

	@MockBean
	private ParticipantRepository participantRepository;

	@Autowired
	private ParticipantService participantService;

	@Test
	void testGetAll() {
		Participant participant = getTestParticipant();
		List<Participant> expected = List.of(participant);

		Mockito.when(participantRepository.findAll()).thenReturn(expected);

		List<Participant> actual = participantService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	void testGetById() {
		int participantId = 1;
		Participant expected = getTestParticipant();
		expected.setId(participantId);

		Mockito.when(participantRepository.findById(participantId))
				.thenReturn(Optional.of(expected));

		Participant actual = participantService.getById(participantId);

		assertEquals(expected, actual);
	}

	@Test
	void testGetByIdThrowsExceptionForNonExistParticipant() {
		int participantId = Integer.MAX_VALUE;

		Mockito.when(participantRepository.findById(participantId))
				.thenThrow(EntityNotFoundServiceException.class);

		assertThrows(EntityNotFoundServiceException.class, 
				() -> participantService.getById(participantId));
	}

	@Test
	void testGetByIdThrowsExceptionForNonValidParticipantId() {
		int participantId = -1;

		assertThrows(ValidationException.class, 
				() -> participantService.getById(participantId));
	}

	@Test
	void testAdd() {
		Participant participant = getTestParticipant();

		participantService.add(participant);

		Mockito.verify(participantRepository).save(participant);
	}

	@Test
	void testAddThrowsExceptionForNullParticipantArgument() {
		Participant participant = null;

		assertThrows(ValidationException.class, () -> participantService.add(participant));
	}

	@Test
	void testRemoveById() {
		int participantId = 1;

		participantService.removeById(participantId);

		Mockito.verify(participantRepository).deleteById(participantId);
	}

}
