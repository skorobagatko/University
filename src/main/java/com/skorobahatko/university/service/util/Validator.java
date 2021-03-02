package com.skorobahatko.university.service.util;

import com.skorobahatko.university.domain.Course;
import com.skorobahatko.university.domain.Lecture;
import com.skorobahatko.university.domain.Participant;
import com.skorobahatko.university.service.exception.ValidationException;

public class Validator {
	
	private Validator() {}

    public static void validateId(int id) {
        if (id <= 0) {
            String message = String.format("ID must be a positive integer value. Actually, ID was %d", id);
            throw new ValidationException(message);
        }
    }

    public static void validateLecture(Lecture lecture) {
        if (lecture == null) {
            String message = String.format("Lecture must not be null. Actually, Lecture was %s", lecture);
            throw new ValidationException(message);
        }
    }

    public static void validateCourse(Course course) {
        if (course == null) {
            String message = String.format("Course must not be null. Actually, Course was %s", course);
            throw new ValidationException(message);
        }
    }

    public static void validateParticipant(Participant participant) {
        if (participant == null) {
            String message = String.format("Participant must not be null. Actually, Participant was %s", participant);
            throw new ValidationException(message);
        }
    }

}
