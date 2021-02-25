package com.skorobahatko.university.domain.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.skorobahatko.university.domain.Course;

public class CourseValidationTest {
	
	private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    
    @Test
    void testValidationForCourseWithEmptyName() {
    	Course course = new Course();
    	course.setName("");
    	Set<ConstraintViolation<Course>> violations = validator.validate(course);
        assertFalse(violations.isEmpty());
    }
    
    @Test
    void testValidationForCourseWithShortName() {
    	Course course = new Course();
    	course.setName("C");
    	Set<ConstraintViolation<Course>> violations = validator.validate(course);
        assertFalse(violations.isEmpty());
    }
    
    @Test
    void testValidationForCourseWithCorrectName() {
    	Course course = new Course();
    	course.setName("Course");
    	Set<ConstraintViolation<Course>> violations = validator.validate(course);
        assertTrue(violations.isEmpty());
    }

}
