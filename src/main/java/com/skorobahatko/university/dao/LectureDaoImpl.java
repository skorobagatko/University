package com.skorobahatko.university.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.skorobahatko.university.dao.exception.DaoException;
import com.skorobahatko.university.dao.exception.EntityNotFoundDaoException;
import com.skorobahatko.university.domain.Lecture;

@Repository("lectureDao")
public class LectureDaoImpl implements LectureDao {
	
	private static final Logger logger = LoggerFactory.getLogger(LectureDaoImpl.class);

	private static final String GET_ALL_JPQL = "select l from Lecture l";

	private static final String GET_BY_ID_JPQL = "select l from Lecture l where l.id = :lectureId";

	private static final String GET_BY_COURSE_ID_JPQL = "select l from Lecture l where l.courseId = :courseId";

	private static final String REMOVE_BY_COURSE_ID_JPQL = "delete from Lecture l where l.courseId = :courseId";
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<Lecture> getAll() {
		logger.debug("Retrieving the lectures list");
		
		try {
			List<Lecture> result = entityManager
					.createQuery(GET_ALL_JPQL, Lecture.class)
					.getResultList();
			
			logger.debug("Retrieved {} lectures", result.size());
			
			return result;
		} catch (HibernateException e) {
			throw new DaoException("Unable to get lectures", e);
		}
	}

	@Override
	public Lecture getById(int id) {
		logger.debug("Retrieving Lecture with id = {}", id);
		
		try {
			Lecture result = entityManager
					.createQuery(GET_BY_ID_JPQL, Lecture.class)
					.setParameter("lectureId", id)
					.setHint("org.hibernate.cacheable", true)
					.getSingleResult();
			
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
	public List<Lecture> getByCourseId(int courseId) {
		logger.debug("Retrieving Lecture for Course with id = {}", courseId);
		
		try {
			TypedQuery<Lecture> query = entityManager.createQuery(GET_BY_COURSE_ID_JPQL, Lecture.class);
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
	public void addAll(List<Lecture> lectures) {
		lectures.forEach(this::add);
	}

	@Override
	public void add(Lecture lecture) {
		logger.debug("Adding Lecture: {}", lecture);
		
		try {
			entityManager.persist(lecture);
			entityManager.flush();
		} catch (HibernateException e) {
			String message = String.format("Unable to add Lecture: %s", lecture);
			throw new DaoException(message, e);
		}
		
		logger.debug("Successfully added Lecture {}", lecture);
	}
	
	@Override
	public void update(Lecture lecture) {
		logger.debug("Updating Lecture: {}", lecture);
		
		try {
			entityManager.merge(lecture);
			entityManager.flush();
		} catch (HibernateException e) {
			String message = String.format("Unable to update Lecture: %s", lecture);
			throw new DaoException(message, e);
		}
		
		logger.debug("Successfully updated Lecture {}", lecture);
	}

	@Override
	public void removeById(int id) {
		logger.debug("Removing Lecture with id = {}", id);
		
		try {
			Lecture lecture = entityManager.find(Lecture.class, id);
			entityManager.remove(lecture);
			entityManager.flush();
		} catch (HibernateException e) {
			String message = String.format("Unable to remove Lecture with id = %d", id);
			throw new DaoException(message, e);
		}
		
		logger.debug("Lecture with id = {} successfully removed", id);
	}
	
	@Override
	public void removeByCourseId(int courseId) {
		logger.debug("Removing Lectures with course id = {}", courseId);
		
		try {
			int affectedRows = entityManager.createQuery(REMOVE_BY_COURSE_ID_JPQL)
					.setParameter("courseId", courseId).executeUpdate();
			
			entityManager.flush();
			
			logger.debug("Removed {} lectures with course id = {}", affectedRows, courseId);
		} catch (HibernateException e) {
			String message = String.format("Unable to remove Lectures with course id = %d", courseId);
			throw new DaoException(message, e);
		}
	}

}
