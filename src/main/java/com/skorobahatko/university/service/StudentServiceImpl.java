package com.skorobahatko.university.service;

import static com.skorobahatko.university.service.util.Validator.*;

import com.skorobahatko.university.domain.Student;
import com.skorobahatko.university.repository.StudentRepository;
import com.skorobahatko.university.service.exception.EntityNotFoundException;
import com.skorobahatko.university.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service("studentService")
@Transactional(readOnly = true)
public class StudentServiceImpl implements StudentService {

    private StudentRepository studentRepository;

    @Autowired
    public void setStudentRepository(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public List<Student> getAll() {
        try {
            return studentRepository.findAll();
        } catch (DataAccessException e) {
            throw new ServiceException("Unable to get students list", e);
        }
    }

    @Override
    public Student getById(int id) throws EntityNotFoundException {
        validateId(id);

        try {
            return studentRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            String message = String.format("Student with id = %d not found", id);
            throw new EntityNotFoundException(message);
        } catch (DataAccessException e) {
            String message = String.format("Unable to get Student with id = %d", id);
            throw new ServiceException(message, e);
        }
    }

    @Override
    @Transactional
    public Student add(Student student) {
        validateParticipant(student);

        try {
            return studentRepository.save(student);
        } catch (DataAccessException e) {
            String message = String.format("Unable to add Student: %s", student);
            throw new ServiceException(message, e);
        }
    }

    @Override
    @Transactional
    public void update(Student student) {
        validateParticipant(student);

        try {
            studentRepository.save(student);
        } catch (DataAccessException e) {
            String message = String.format("Unable to update Student: %s", student);
            throw new ServiceException(message, e);
        }
    }

    @Override
    @Transactional
    public void removeById(int id) {
        validateId(id);

        try {
            studentRepository.deleteById(id);
        } catch (DataAccessException e) {
            String message = String.format("Unable to remove Student with id = %d", id);
            throw new ServiceException(message, e);
        }
    }

    @Override
    @Transactional
    public void addCourseToStudentById(int studentId, int courseId) {
        validateId(studentId);
		validateId(courseId);

		try {
            studentRepository.addCourseToStudentById(studentId, courseId);
		} catch (DataAccessException e) {
			String message = String.format("Unable to add Course with id = %d to Student with id = %d",
					courseId, studentId);
			throw new ServiceException(message, e);
		}
    }

    @Override
    @Transactional
    public void deleteStudentsCourseById(int studentId, int courseId) {
        validateId(studentId);
		validateId(courseId);

		try {
            studentRepository.deleteStudentsCourseById(studentId, courseId);
		} catch (DataAccessException e) {
			String message = String.format("Unable to remove Course with id = %d for Student with id = %d",
					courseId, studentId);
			throw new ServiceException(message, e);
		}
    }

}
