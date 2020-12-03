package com.skorobahatko.university.dao;

import java.util.List;
import java.util.Optional;

public interface BaseDao<T> {
	
	List<T> getAll();
	Optional<T> getById(int id);
	void add(T t);
	void removeById(int id);

}
