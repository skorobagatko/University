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
import com.skorobahatko.university.domain.Lecture;

public class LectureDaoImpl implements LectureDao {
	
	private static final Logger logger = LoggerFactory.getLogger(LectureDaoImpl.class);

	private static final String GET_ALL_HQL = "from Lecture";

	private static final String GET_BY_ID_HQL = "from Lecture l where l.id = :lId";

	private static final String GET_BY_COURSE_ID_HQL = "from Lecture l where l.courseId = :courseId";

	private static final String REMOVE_BY_COURSE_ID_HQL = "delete from Lecture l where l.courseId = :courseId";
	
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	@Transactional
	public List<Lecture> getAll() {
		logger.debug("Retrieving the lectures list");
		
		try {
			Session session = sessionFactory.getCurrentSession();
			List<Lecture> result = session.createQuery(GET_ALL_HQL, Lecture.class).getResultList();
			
			logger.debug("Retrieved {} lectures", result.size());
			
			return result;
		} catch (HibernateException e) {
			throw new DaoException("Unable to get lectures", e);
		}
	}

	@Override
	@Transactional
	public Lecture getById(int id) {
		logger.debug("Retrieving Lecture with id = {}", id);
		
		try {
			Session session = sessionFactory.getCurrentSession();
			Query<Lecture> query = session.createQuery(GET_BY_ID_HQL, Lecture.class);
			query.setParameter("lId", id);
			Lecture result = query.getSingleResult();
			
			logger.debug("Lecture with id = {} successfully retrieved", id);
			
			return result;
		} catch (NoResultException e) {
			String message = String.format("Lecture with id = %d not found", id);
			throw new EntityNotFoundDaoException(message, e);
		} catch (HibernateException e) {
			String message = String.format("Unable to get Lecture with id = %d", id);
			throw new DaoException(message, e);
		}
	}

	@Override
	@Transactional
	public List<Lecture> getByCourseId(int courseId) {
		logger.debug("Retrieving Lecture for Course with id = {}", courseId);
		
		try {
			Session session = sessionFactory.getCurrentSession();
			Query<Lecture> query = session.createQuery(GET_BY_COURSE_ID_HQL, Lecture.class);
			query.setParameter("courseId", courseId);
			List<Lecture> result = query.getResultList();
			
			logger.debug("Lectures list for Course with id = {} successfully retrieved", courseId);
			
			return result;
		} catch (HibernateException e) {
			String message = String.format("Unable to get lectures for Course with id = %d", courseId);
			throw new DaoException(message, e);
		}
	}
	
	@Override
	@Transactional
	public void addAll(List<Lecture> lectures) {
		lectures.forEach(this::add);
	}

	@Override
	@Transactional
	public void add(Lecture lecture) {
		logger.debug("Adding Lecture: {}", lecture);
		
		try {
			Session session = sessionFactory.getCurrentSession();
			int lectureId = (int) session.save(lecture);
			lecture.setId(lectureId);
		} catch (HibernateException e) {
			String message = String.format("Unable to add Lecture: %s", lecture);
			throw new DaoException(message, e);
		}
		
		logger.debug("Successfully added Lecture {}", lecture);
	}
	
	@Override
	@Transactional
	public void update(Lecture lecture) {
		logger.debug("Updating Lecture: {}", lecture);
		
		try {
			Session session = sessionFactory.getCurrentSession();
			session.update(lecture);
		} catch (HibernateException e) {
			String message = String.format("Unable to update Lecture: %s", lecture);
			throw new DaoException(message, e);
		}
		
		logger.debug("Successfully updated Lecture {}", lecture);
	}

	@Override
	@Transactional
	public void removeById(int id) {
		logger.debug("Removing Lecture with id = {}", id);
		
		try {
			Session session = sessionFactory.getCurrentSession();
			Lecture lecture = session.byId(Lecture.class).load(id);
			session.delete(lecture);
		} catch (HibernateException e) {
			String message = String.format("Unable to remove Lecture with id = %d", id);
			throw new DaoException(message, e);
		}
		
		logger.debug("Lecture with id = {} successfully removed", id);
	}
	
	@Override
	@Transactional
	public void removeByCourseId(int courseId) {
		logger.debug("Removing Lectures with course id = {}", courseId);
		
		try {
			Session session = sessionFactory.getCurrentSession();
			int affectedRows = session.createQuery(REMOVE_BY_COURSE_ID_HQL)
					.setParameter("courseId", courseId).executeUpdate();
			
			logger.debug("Removed {} lectures with course id = {}", affectedRows, courseId);
		} catch (HibernateException e) {
			String message = String.format("Unable to remove Lectures with course id = %d", courseId);
			throw new DaoException(message, e);
		}
	}

}
