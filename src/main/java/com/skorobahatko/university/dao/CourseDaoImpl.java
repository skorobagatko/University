package com.skorobahatko.university.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skorobahatko.university.dao.exception.DaoException;
import com.skorobahatko.university.dao.exception.EntityNotFoundDaoException;
import com.skorobahatko.university.domain.Course;

public class CourseDaoImpl implements CourseDao {

	private static final Logger logger = LoggerFactory.getLogger(CourseDaoImpl.class);

	private static final String GET_ALL_HQL = "from Course";

	private static final String GET_BY_ID_HQL = "from Course c where c.id = :cId";

	private static final String REMOVE_BY_ID_HQL = "delete from Course c where c.id = :id";

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	@Transactional
	public List<Course> getAll() {
		logger.debug("Retrieving courses list");

		try {
			Session session = sessionFactory.getCurrentSession();
			List<Course> result = session.createQuery(GET_ALL_HQL, Course.class).getResultList();

			logger.debug("Retrieved {} Courses", result.size());

			return result;
		} catch (HibernateException e) {
			throw new DaoException("Unable to get courses", e);
		}
	}

	@Override
	@Transactional
	public Course getById(int id) {
		logger.debug("Retrieving Course with id = {}", id);

		try {
			Session session = sessionFactory.getCurrentSession();
			Query<Course> query = session.createQuery(GET_BY_ID_HQL, Course.class);
			query.setParameter("cId", id);
			Course result = query.getSingleResult();

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
	@Transactional
	public void add(Course course) {
		checkForNull(course);

		logger.debug("Adding Course: {}", course);

		try {
			Session session = sessionFactory.getCurrentSession();
			int courseId = (int) session.save(course);
			course.setId(courseId);
		} catch (HibernateException e) {
			String message = String.format("Unable to add Course: %s", course);
			throw new DaoException(message, e);
		}

		logger.debug("Successfully added Course {}", course);
	}

	@Override
	@Transactional
	public void update(Course course) {
		checkForNull(course);

		logger.debug("Updating Course: {}", course);

		try {
			Session session = sessionFactory.getCurrentSession();
			session.merge(course);
		} catch (HibernateException e) {
			String message = String.format("Unable to update Course: %s", course);
			throw new DaoException(message, e);
		}

		logger.debug("Successfully updated Course {}", course);
	}

	@Override
	@Transactional
	public void removeById(int id) {
		logger.debug("Removing Course with id = {}", id);

		int affectedRows = 0;

		try {
			Session session = sessionFactory.getCurrentSession();
			affectedRows = session.createQuery(REMOVE_BY_ID_HQL)
					.setParameter("id", id)
					.executeUpdate();

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
