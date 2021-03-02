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

import com.skorobahatko.university.domain.Participant;
import com.skorobahatko.university.domain.Student;

class ParticipantValidationTest {
	
	private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    
    @Test
    void testValidationForParticipantWithCorrectFileds() {
    	Participant participant = new Student();
    	participant.setFirstName("Student");
    	participant.setLastName("Student");
    	
    	Set<ConstraintViolation<Participant>> violations = validator.validate(participant);
    	
        assertTrue(violations.isEmpty());
    }
    
    @Test
    void testValidationForParticipantWithEmptyFirstName() {
    	Participant participant = new Student();
    	participant.setFirstName(null);
    	participant.setLastName("Student");
    	
    	Set<ConstraintViolation<Participant>> violations = validator.validate(participant);
    	
        assertFalse(violations.isEmpty());
    }
    
    @Test
    void testValidationForParticipantWithShortFirstName() {
    	Participant participant = new Student();
    	participant.setFirstName("S");
    	participant.setLastName("Student");
    	
    	Set<ConstraintViolation<Participant>> violations = validator.validate(participant);
    	
        assertFalse(violations.isEmpty());
    }
    
    @Test
    void testValidationForParticipantWithEmptyLastName() {
    	Participant participant = new Student();
    	participant.setFirstName("Student");
    	participant.setLastName(null);
    	
    	Set<ConstraintViolation<Participant>> violations = validator.validate(participant);
    	
        assertFalse(violations.isEmpty());
    }
    
    @Test
    void testValidationForParticipantWithShortLastName() {
    	Participant participant = new Student();
    	participant.setFirstName("Student");
    	participant.setLastName("S");
    	
    	Set<ConstraintViolation<Participant>> violations = validator.validate(participant);
    	
        assertFalse(violations.isEmpty());
    }

}
