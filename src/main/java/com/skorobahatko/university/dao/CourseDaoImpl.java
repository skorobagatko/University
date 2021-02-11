package com.skorobahatko.university.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.skorobahatko.university.dao.exception.DaoException;
import com.skorobahatko.university.dao.exception.EntityNotFoundDaoException;
import com.skorobahatko.university.domain.Course;

@Repository("courseDao")
public class CourseDaoImpl implements CourseDao {

	private static final Logger logger = LoggerFactory.getLogger(CourseDaoImpl.class);

	private static final String GET_ALL_JPQL = "select c from Course c";

	private static final String GET_BY_ID_JPQL = "select c from Course c where c.id = :courseId";

	private static final String REMOVE_BY_ID_JPQL = "delete from Course c where c.id = :courseId";

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<Course> getAll() {
		logger.debug("Retrieving courses list");

		try {
			List<Course> result = entityManager.createQuery(GET_ALL_JPQL, Course.class)
					.getResultList();

			logger.debug("Retrieved {} Courses", result.size());

			return result;
		} catch (HibernateException e) {
			throw new DaoException("Unable to get courses", e);
		}
	}

	@Override
	public Course getById(int id) {
		logger.debug("Retrieving Course with id = {}", id);

		try {
			Course result = entityManager
					.createQuery(GET_BY_ID_JPQL, Course.class)
					.setParameter("courseId", id)
					.setHint("org.hibernate.cacheable", true)
					.getSingleResult();

			logger.debug("Course with id = {} successfully retrieved", id);

			return result;
		} catch (NoResultException e) {
			String message = String.format("Course with id = %d not found", id);
			throw new EntityNotFoundDaoException(message, e);
		} catch (HibernateException e) {
			throw new DaoException("Unable to get courses", e);
		}
	}

	@Override
	public void add(Course course) {
		checkForNull(course);

		logger.debug("Adding Course: {}", course);

		try {
			entityManager.persist(course);
			entityManager.flush();
		} catch (HibernateException e) {
			String message = String.format("Unable to add Course: %s", course);
			throw new DaoException(message, e);
		}

		logger.debug("Successfully added Course {}", course);
	}

	@Override
	public void update(Course course) {
		checkForNull(course);

		logger.debug("Updating Course: {}", course);

		try {
			entityManager.merge(course);
			entityManager.flush();
		} catch (HibernateException e) {
			String message = String.format("Unable to update Course: %s", course);
			throw new DaoException(message, e);
		}

		logger.debug("Successfully updated Course {}", course);
	}

	@Override
	public void removeById(int id) {
		logger.debug("Removing Course with id = {}", id);

		int affectedRows = 0;

		try {
			affectedRows = entityManager.createQuery(REMOVE_BY_ID_JPQL)
					.setParameter("courseId", id)
					.executeUpdate();
			
			entityManager.flush();

			logger.debug("Course with id = {} successfully removed", id);
		} catch (HibernateException e) {
			String message = String.format("Unable to remove Course with id = %d", id);
			throw new DaoException(message, e);
		}

		if (affectedRows == 0) {
			String message = String.format("Course with id = %d not found", id);
			throw new EntityNotFoundDaoException(message);
		}
	}

	private void checkForNull(Course course) {
		if (course == null) {
			throw new DaoException("The input argument must no be null");
		}
	}

}
