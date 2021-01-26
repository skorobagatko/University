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
import com.skorobahatko.university.domain.Participant;
import com.skorobahatko.university.domain.Student;
import com.skorobahatko.university.domain.Teacher;

public class ParticipantDaoImpl implements ParticipantDao {
	
	private static final Logger logger = LoggerFactory.getLogger(ParticipantDaoImpl.class);

	private static final String GET_ALL_HQL = "from Participant";
	
	private static final String GET_ALL_STUDENTS_HQL = "from Student";
	
	private static final String GET_ALL_TEACHERS_HQL = "from Teacher";

	private static final String GET_BY_ID_HQL = "from Participant p where p.id = :pId";

	private static final String ADD_PARTICIPANT_COURSE_PAIR_SQL = "INSERT INTO participants_courses "
			+ "(participant_id, course_id) VALUES (?, ?);";
	
	private static final String DELETE_PARTICIPANT_COURSE_PAIR_SQL = "DELETE FROM participants_courses "
			+ "WHERE participant_id = ? AND course_id = ?;";

	private static final String REMOVE_BY_ID_HQL = "delete from Participant p where p.id = :id";
	
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	@Transactional
	public List<Participant> getAll() {
		logger.debug("Retrieving the participants list");
		
		try {
			Session session = sessionFactory.getCurrentSession();
			List<Participant> result = session.createQuery(GET_ALL_HQL, Participant.class).getResultList();

			logger.debug("Retrieved {} participants", result.size());

			return result;
		} catch (HibernateException e) {
			throw new DaoException("Unable to get participants", e);
		}
	}
	
	@Override
	@Transactional
	public List<Student> getAllStudents() {
		logger.debug("Retrieving the Student list");
		
		try {
			Session session = sessionFactory.getCurrentSession();
			List<Student> result = session.createQuery(GET_ALL_STUDENTS_HQL, Student.class).getResultList();

			logger.debug("Retrieved {} students", result.size());

			return result;
		} catch (HibernateException e) {
			throw new DaoException("Unable to get Student list", e);
		}
	}
	
	@Override
	@Transactional
	public List<Teacher> getAllTeachers() {
		logger.debug("Retrieving the Teacher list");
		
		try {
			Session session = sessionFactory.getCurrentSession();
			List<Teacher> result = session.createQuery(GET_ALL_TEACHERS_HQL, Teacher.class).getResultList();

			logger.debug("Retrieved {} teachers", result.size());

			return result;
		} catch (HibernateException e) {
			throw new DaoException("Unable to get Teacher list", e);
		}
	}

	@Override
	@Transactional
	public Participant getById(int id) {
		logger.debug("Retrieving Participant with id = {}", id);
		
		try {
			Session session = sessionFactory.getCurrentSession();
			Query<Participant> query = session.createQuery(GET_BY_ID_HQL, Participant.class);
			query.setParameter("pId", id);
			Participant result = query.getSingleResult();

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
	@Transactional
	public void add(Participant participant) {
		logger.debug("Adding Participant: {}", participant);
		
		checkForNull(participant);
		
		try {
			Session session = sessionFactory.getCurrentSession();
			int participantId = (int) session.save(participant);
			participant.setId(participantId);
		} catch (HibernateException e) {
			String message = String.format("Unable to add Participant: %s", participant);
			throw new DaoException(message, e);
		}
		
		logger.debug("Successfully added Participant {}", participant);
	}
	
	@Override
	@Transactional
	public void update(Participant participant) {
		logger.debug("Updating Participant: {}", participant);
		
		checkForNull(participant);
		
		try {
			Session session = sessionFactory.getCurrentSession();
			session.merge(participant);
		} catch (HibernateException e) {
			String message = String.format("Unable to update Participant: %s", participant);
			throw new DaoException(message, e);
		}
		
		logger.debug("Successfully updated Participant {}", participant);
	}

	@Override
	@Transactional
	public void removeById(int id) {
		logger.debug("Removing Participant with id = {}", id);
		
		int affectedRows = 0;
		
		try {
			Session session = sessionFactory.getCurrentSession();
			affectedRows = session.createQuery(REMOVE_BY_ID_HQL)
					.setParameter("id", id)
					.executeUpdate();

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
	@Transactional
	public void addParticipantCourseById(int participantId, int courseId) {
		logger.debug("Adding Course with id = {} to Participant with id = {}", courseId, participantId);
		
		try {
			Session session = sessionFactory.getCurrentSession();
			session.createSQLQuery(ADD_PARTICIPANT_COURSE_PAIR_SQL)
					.setParameter(1, participantId)
					.setParameter(2, courseId)
					.executeUpdate();
		} catch (HibernateException e) {
			String message = String.format("Unable to add Course with id = %d to Participant with id = %d", 
					courseId, participantId);
			throw new DaoException(message, e);
		}
		
		logger.debug("Course with id = {} successfully added to Participant with id = {}", courseId, participantId);
	}

	@Override
	@Transactional
	public void removeParticipantCourseById(int participantId, int courseId) {
		logger.debug("Removing Course with id = {} for Participant with id = {}", courseId, participantId);
		
		try {
			Session session = sessionFactory.getCurrentSession();
			session.createSQLQuery(DELETE_PARTICIPANT_COURSE_PAIR_SQL)
					.setParameter(1, participantId)
					.setParameter(2, courseId)
					.executeUpdate();
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
