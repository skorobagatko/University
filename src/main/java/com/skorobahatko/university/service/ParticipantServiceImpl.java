package com.skorobahatko.university.service;

import static com.skorobahatko.university.service.util.Validator.*;

import com.skorobahatko.university.domain.Participant;
import com.skorobahatko.university.repository.ParticipantRepository;
import com.skorobahatko.university.service.exception.EntityNotFoundException;
import com.skorobahatko.university.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service("participantService")
@Transactional(readOnly = true)
public class ParticipantServiceImpl implements ParticipantService {
	
	private ParticipantRepository participantRepository;
	
	@Autowired
	public ParticipantServiceImpl(ParticipantRepository participantRepository) {
		this.participantRepository = participantRepository;
	}

	@Override
	public List<Participant> getAll() {
		try {
			return participantRepository.findAll();
		} catch (DataAccessException e) {
			throw new ServiceException("Unable to get participants list", e);
		}
	}
	
	@Override
	public Participant getById(int id) {
		validateId(id);
		
		try {
			return participantRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			String message = String.format("Participant with id = %d not found", id);
			throw new EntityNotFoundException(message);
		} catch (DataAccessException e) {
			String message = String.format("Unable to get Participant with id = %d", id);
			throw new ServiceException(message, e);
		}
	}

	@Override
	@Transactional
	public Participant add(Participant participant) {
		validateParticipant(participant);
		
		try {
			return participantRepository.save(participant);
		} catch (DataAccessException e) {
			String message = String.format("Unable to add Participant: %s", participant);
			throw new ServiceException(message, e);
		}
	}
	
	@Override
	@Transactional
	public void update(Participant participant) {
		validateParticipant(participant);
		
		try {
			participantRepository.save(participant);
		} catch (DataAccessException e) {
			String message = String.format("Unable to update Participant: %s", participant);
			throw new ServiceException(message, e);
		}
	}

	@Override
	@Transactional
	public void removeById(int id) {
		validateId(id);
		
		try {
			participantRepository.deleteById(id);
		} catch (DataAccessException e) {
			String message = String.format("Unable to remove Participant with id = %d", id);
			throw new ServiceException(message, e);
		}
	}

}
