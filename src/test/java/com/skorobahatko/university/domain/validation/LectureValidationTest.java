package com.skorobahatko.university.domain.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.skorobahatko.university.domain.Lecture;

public class LectureValidationTest {
	
	private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    
    @Test
    void testValidationForLectureWithCorrectFields() {
    	Lecture lecture = new Lecture();
    	lecture.setName("Lecture");
    	lecture.setDate(LocalDate.now());
    	lecture.setStartTime(LocalTime.now());
    	lecture.setEndTime(LocalTime.now());
    	lecture.setRoomNumber(1);
    	
    	Set<ConstraintViolation<Lecture>> violations = validator.validate(lecture);
    	
        assertTrue(violations.isEmpty());
    }
    
    @Test
    void testValidationForLectureWithEmptyName() {
    	Lecture lecture = new Lecture();
    	lecture.setName("");
    	lecture.setDate(LocalDate.now());
    	lecture.setStartTime(LocalTime.now());
    	lecture.setEndTime(LocalTime.now());
    	lecture.setRoomNumber(1);
    	
    	Set<ConstraintViolation<Lecture>> violations = validator.validate(lecture);
    	
        assertFalse(violations.isEmpty());
    }

    @Test
    void testValidationForLectureWithShortName() {
    	Lecture lecture = new Lecture();
    	lecture.setName("L");
    	lecture.setDate(LocalDate.now());
    	lecture.setStartTime(LocalTime.now());
    	lecture.setEndTime(LocalTime.now());
    	lecture.setRoomNumber(1);
    	
    	Set<ConstraintViolation<Lecture>> violations = validator.validate(lecture);
    	
        assertFalse(violations.isEmpty());
    }
    
    @Test
    void testValidationForLectureWithEmptyDate() {
    	Lecture lecture = new Lecture();
    	lecture.setName("Lecture");
    	lecture.setDate(null);
    	lecture.setStartTime(LocalTime.now());
    	lecture.setEndTime(LocalTime.now());
    	lecture.setRoomNumber(1);
    	
    	Set<ConstraintViolation<Lecture>> violations = validator.validate(lecture);
    	
        assertFalse(violations.isEmpty());
    }
    
    @Test
    void testValidationForLectureWithEmptyStartTime() {
    	Lecture lecture = new Lecture();
    	lecture.setName("Lecture");
    	lecture.setDate(LocalDate.now());
    	lecture.setStartTime(null);
    	lecture.setEndTime(LocalTime.now());
    	lecture.setRoomNumber(1);
    	
    	Set<ConstraintViolation<Lecture>> violations = validator.validate(lecture);
    	
        assertFalse(violations.isEmpty());
    }
    
    @Test
    void testValidationForLectureWithEmptyEndTime() {
    	Lecture lecture = new Lecture();
    	lecture.setName("Lecture");
    	lecture.setDate(LocalDate.now());
    	lecture.setStartTime(LocalTime.now());
    	lecture.setEndTime(null);
    	lecture.setRoomNumber(1);
    	
    	Set<ConstraintViolation<Lecture>> violations = validator.validate(lecture);
    	
        assertFalse(violations.isEmpty());
    }
    
    @Test
    void testValidationForLectureWithNonCorrectRoomNumber() {
    	Lecture lecture = new Lecture();
    	lecture.setRoomNumber(-1);
    	Set<ConstraintViolation<Lecture>> violations = validator.validate(lecture);
        assertFalse(violations.isEmpty());
    }
    
}
