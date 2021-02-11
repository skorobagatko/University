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
import com.skorobahatko.university.domain.Participant;
import com.skorobahatko.university.domain.Student;
import com.skorobahatko.university.domain.Teacher;

@Repository("participantDao")
public class ParticipantDaoImpl implements ParticipantDao {
	
	private static final Logger logger = LoggerFactory.getLogger(ParticipantDaoImpl.class);

	private static final String GET_ALL_JPQL = "select p from Participant p";
	
	private static final String GET_ALL_STUDENTS_JPQL = "select s from Student s";
	
	private static final String GET_ALL_TEACHERS_JPQL = "select t from Teacher t";

	private static final String GET_BY_ID_JPQL = "select p from Participant p where p.id = :pId";

	private static final String REMOVE_BY_ID_JPQL = "delete from Participant p where p.id = :pId";
	
	private static final String ADD_PARTICIPANT_COURSE_PAIR_SQL = "INSERT INTO participants_courses "
			+ "(participant_id, course_id) VALUES (?, ?);";
	
	private static final String DELETE_PARTICIPANT_COURSE_PAIR_SQL = "DELETE FROM participants_courses "
			+ "WHERE participant_id = ? AND course_id = ?;";

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<Participant> getAll() {
		logger.debug("Retrieving the participants list");
		
		try {
			List<Participant> result = entityManager.createQuery(GET_ALL_JPQL, Participant.class).getResultList();

			logger.debug("Retrieved {} participants", result.size());

			return result;
		} catch (HibernateException e) {
			throw new DaoException("Unable to get participants", e);
		}
	}
	
	@Override
	public List<Student> getAllStudents() {
		logger.debug("Retrieving the Student list");
		
		try {
			List<Student> result = entityManager.createQuery(GET_ALL_STUDENTS_JPQL, Student.class).getResultList();

			logger.debug("Retrieved {} students", result.size());

			return result;
		} catch (HibernateException e) {
			throw new DaoException("Unable to get Student list", e);
		}
	}
	
	@Override
	public List<Teacher> getAllTeachers() {
		logger.debug("Retrieving the Teacher list");
		
		try {
			List<Teacher> result = entityManager
					.createQuery(GET_ALL_TEACHERS_JPQL, Teacher.class)
					.getResultList();

			logger.debug("Retrieved {} teachers", result.size());

			return result;
		} catch (HibernateException e) {
			throw new DaoException("Unable to get Teacher list", e);
		}
	}

	@Override
	public Participant getById(int id) {
		logger.debug("Retrieving Participant with id = {}", id);
		
		try {
			Participant result = entityManager
					.createQuery(GET_BY_ID_JPQL, Participant.class)
					.setParameter("pId", id)
					.setHint("org.hibernate.cacheable", true)
					.getSingleResult();

			logger.debug("Participant with id = {} successfully retrieved", id);

			return result;
		} catch (NoResultException e) {
			String message = String.format("Participant with id = %d not found", id);
			throw new EntityNotFoundDaoException(message, e);
		} catch (HibernateException e) {
			String message = String.format("Unable to get Participant with id = %d", id);
			throw new DaoException(message, e);
		}
	}

	@Override
	public void add(Participant participant) {
		logger.debug("Adding Participant: {}", participant);
		
		checkForNull(participant);
		
		try {
			entityManager.persist(participant);
			entityManager.flush();
		} catch (HibernateException e) {
			String message = String.format("Unable to add Participant: %s", participant);
			throw new DaoException(message, e);
		}
		
		logger.debug("Successfully added Participant {}", participant);
	}
	
	@Override
	public void update(Participant participant) {
		logger.debug("Updating Participant: {}", participant);
		
		checkForNull(participant);
		
		try {
			entityManager.merge(participant);
			entityManager.flush();
		} catch (HibernateException e) {
			String message = String.format("Unable to update Participant: %s", participant);
			throw new DaoException(message, e);
		}
		
		logger.debug("Successfully updated Participant {}", participant);
	}

	@Override
	public void removeById(int id) {
		logger.debug("Removing Participant with id = {}", id);
		
		int affectedRows = 0;
		
		try {
			affectedRows = entityManager.createQuery(REMOVE_BY_ID_JPQL)
					.setParameter("pId", id)
					.executeUpdate();
			
			entityManager.flush();

			logger.debug("Participant with id = {} successfully removed", id);
		} catch (HibernateException e) {
			String message = String.format("Unable to remove Participant with id = %d", id);
			throw new DaoException(message, e);
		}
		
		if (affectedRows == 0) {
			String message = String.format("Participant with id = %d not found", id);
			throw new EntityNotFoundDaoException(message);
		}
	}
	
	@Override
	public void addParticipantCourseById(int participantId, int courseId) {
		logger.debug("Adding Course with id = {} to Participant with id = {}", courseId, participantId);
		
		try {
			entityManager.createNativeQuery(ADD_PARTICIPANT_COURSE_PAIR_SQL)
					.setParameter(1, participantId)
					.setParameter(2, courseId)
					.executeUpdate();
			
			entityManager.flush();
		} catch (HibernateException e) {
			String message = String.format("Unable to add Course with id = %d to Participant with id = %d", 
					courseId, participantId);
			throw new DaoException(message, e);
		}
		
		logger.debug("Course with id = {} successfully added to Participant with id = {}", courseId, participantId);
	}

	@Override
	public void removeParticipantCourseById(int participantId, int courseId) {
		logger.debug("Removing Course with id = {} for Participant with id = {}", courseId, participantId);
		
		try {
			entityManager.createNativeQuery(DELETE_PARTICIPANT_COURSE_PAIR_SQL)
					.setParameter(1, participantId)
					.setParameter(2, courseId)
					.executeUpdate();
			
			entityManager.flush();
		} catch (HibernateException e) {
			String message = String.format("Unable to remove Course with id = %d for Participant with id = %d", 
					courseId, participantId);
			throw new DaoException(message, e);
		}
		
		logger.debug("Course with id = {} for Participant with id = {} successfully removed", courseId, participantId);
	}

	private void checkForNull(Participant participant) {
		if (participant == null) {
			throw new DaoException("The input argument must no be null");
		}
	}

}
