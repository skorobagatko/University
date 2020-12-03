package com.skorobahatko.university.service;

import java.util.List;
import java.util.Optional;

import com.skorobahatko.university.dao.ParticipantDao;
import com.skorobahatko.university.domain.Participant;

public class ParticipantServiceImpl implements ParticipantService {
	
	private ParticipantDao participantDao;
	
	public void setParticipantDao(ParticipantDao participantDao) {
		this.participantDao = participantDao;
	}

	@Override
	public List<Participant> getAll() {
		return participantDao.getAll();
	}

	@Override
	public Optional<Participant> getById(int id) {
		return participantDao.getById(id);
	}

	@Override
	public void add(Participant participant) {
		participantDao.add(participant);
	}

	@Override
	public void removeById(int id) {
		participantDao.removeById(id);
	}

}
