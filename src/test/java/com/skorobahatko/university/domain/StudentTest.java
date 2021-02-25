package com.skorobahatko.university.domain;

import static com.skorobahatko.university.util.TestUtils.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentTest {
	
	private Student student;

	@BeforeEach
	void setUp() throws Exception {
		student = getTestStudent();
	}

	@Test
	void testAddCourse() {
		Course course = getTestCourse();
		
		assertFalse(student.getCourses().contains(course));
		
		student.addCourse(course);
		
		assertTrue(student.getCourses().contains(course));
	}

	@Test
	void testRemoveCourse() {
		Course course = getTestCourse();
		student.addCourse(course);
		
		student.removeCourse(course);
		
		assertTrue(!student.getCourses().contains(course));
	}
	
	@Test
	void testEqualsMethodReturnsTrueForEqualStudents() {
		Student other = getTestStudent();
		
		assertEquals(student, other);
	}
	
	@Test
	void testEqualsMethodReturnsFalseForNonEqualStudents() {
		int id = 123456789;
		String firstName = "Test";
		String lastName = "Test";
		Student other = new Student(id, firstName, lastName);
		
		assertNotEquals(student, other);
	}
	
	@Test
	void testEqualsMethodReturnsTrueForSameStudents() {
		Student other = student;
		
		assertEquals(student, other);
	}
	
	@Test
	void testEqualsMethodReturnsFalseForNullArgument() {
		Student other = null;
		
		assertNotEquals(student, other);
	}
	
	@Test
	void testEqualsMethodReturnsFalseWithObjectArgument() {
		Object other = new Object();
		
		assertNotEquals(student, other);
	}

}
