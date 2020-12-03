package com.skorobahatko.university.service;

import java.util.List;
import java.util.Optional;

import com.skorobahatko.university.dao.TimetableDao;
import com.skorobahatko.university.domain.Timetable;

public class TimetableServiceImpl implements TimetableService {
	
	private TimetableDao timetableDao;
	
	public void setTimetableDao(TimetableDao timetableDao) {
		this.timetableDao = timetableDao;
	}

	@Override
	public List<Timetable> getAll() {
		return timetableDao.getAll();
	}

	@Override
	public Optional<Timetable> getById(int id) {
		return timetableDao.getById(id);
	}

	@Override
	public void add(Timetable timetable) {
		timetableDao.add(timetable);
	}

	@Override
	public void removeById(int id) {
		timetableDao.removeById(id);
	}

}
