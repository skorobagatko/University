package com.skorobahatko.university.service;

import static com.skorobahatko.university.service.util.Validator.*;

import com.skorobahatko.university.domain.Teacher;
import com.skorobahatko.university.repository.TeacherRepository;
import com.skorobahatko.university.service.exception.EntityNotFoundException;
import com.skorobahatko.university.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service("teacherService")
@Transactional(readOnly = true)
public class TeacherServiceImpl implements TeacherService {

    private TeacherRepository teacherRepository;

    @Autowired
    public void setTeacherRepository(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Override
    public List<Teacher> getAll() {
        try {
            return teacherRepository.findAll();
        } catch (DataAccessException e) {
            throw new ServiceException("Unable to get teachers list", e);
        }
    }

    @Override
    public Teacher getById(int id) throws EntityNotFoundException {
        validateId(id);

        try {
            return teacherRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            String message = String.format("Teacher with id = %d not found", id);
            throw new EntityNotFoundException(message);
        } catch (DataAccessException e) {
            String message = String.format("Unable to get Teacher with id = %d", id);
            throw new ServiceException(message, e);
        }
    }

    @Override
    @Transactional
    public Teacher add(Teacher teacher) {
        validateParticipant(teacher);

        try {
            return teacherRepository.save(teacher);
        } catch (DataAccessException e) {
            String message = String.format("Unable to add Teacher: %s", teacher);
            throw new ServiceException(message, e);
        }
    }

    @Override
    @Transactional
    public void update(Teacher teacher) {
        validateParticipant(teacher);

        try {
            teacherRepository.save(teacher);
        } catch (DataAccessException e) {
            String message = String.format("Unable to update Teacher: %s", teacher);
            throw new ServiceException(message, e);
        }
    }

    @Override
    @Transactional
    public void removeById(int id) {
        validateId(id);

        try {
            teacherRepository.deleteById(id);
        } catch (DataAccessException e) {
            String message = String.format("Unable to remove Teacher with id = %d", id);
            throw new ServiceException(message, e);
        }
    }

    @Override
    @Transactional
    public void addCourseToTeacherById(int teacherId, int courseId) {
        validateId(teacherId);
        validateId(courseId);

        try {
            teacherRepository.addCourseToTeacherById(teacherId, courseId);
        } catch (DataAccessException e) {
            String message = String.format("Unable to add Course with id = %d to Teacher with id = %d",
                    courseId, teacherId);
            throw new ServiceException(message, e);
        }
    }

    @Override
    @Transactional
    public void deleteTeachersCourseById(int teacherId, int courseId) {
        validateId(teacherId);
        validateId(courseId);

        try {
            teacherRepository.deleteTeachersCourseById(teacherId, courseId);
        } catch (DataAccessException e) {
            String message = String.format("Unable to remove Course with id = %d for Teacher with id = %d",
                    courseId, teacherId);
            throw new ServiceException(message, e);
        }
    }

}
