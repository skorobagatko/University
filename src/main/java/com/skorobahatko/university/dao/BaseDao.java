package com.skorobahatko.university.dao;

import java.util.List;

public interface BaseDao<T> {
	
	List<T> getAll();
	T getById(int id);
	void add(T t);
	void removeById(int id);

}
