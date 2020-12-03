package com.skorobahatko.university.service;

import java.util.List;
import java.util.Optional;

public interface BaseService<T> {
	
	List<T> getAll();
	Optional<T> getById(int id);
	void add(T t);
	void removeById(int id);

}
