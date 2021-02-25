package com.skorobahatko.university.domain;

import static com.skorobahatko.university.util.TestUtils.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TeacherTest {
	
	private Teacher teacher;

	@BeforeEach
	void setUp() throws Exception {
		teacher = getTestTeacher();
	}

	@Test
	void testAddCourse() {
		Course course = getTestCourse();
		
		assertFalse(teacher.getCourses().contains(course));
		
		teacher.addCourse(course);
		
		assertTrue(teacher.getCourses().contains(course));
	}

	@Test
	void testRemoveCourse() {
		Course course = getTestCourse();
		teacher.addCourse(course);
		
		teacher.removeCourse(course);
		
		assertTrue(!teacher.getCourses().contains(course));
	}
	
	@Test
	void testEqualsMethodReturnsTrueForEqualTeachers() {
		Teacher other = getTestTeacher();
		
		assertEquals(teacher, other);
	}
	
	@Test
	void testEqualsMethodReturnsFalseForNonEqualTeachers() {
		int id = 123456789;
		String firstName = "Test";
		String lastName = "Test";
		Teacher other = new Teacher(id, firstName, lastName);
		
		assertNotEquals(teacher, other);
	}
	
	@Test
	void testEqualsMethodReturnsTrueForSameTeachers() {
		Teacher other = teacher;
		
		assertEquals(teacher, other);
	}
	
	@Test
	void testEqualsMethodReturnsFalseForNullArgument() {
		Student other = null;
		
		assertNotEquals(teacher, other);
	}
	
	@Test
	void testEqualsMethodReturnsFalseWithObjectArgument() {
		Object other = new Object();
		
		assertNotEquals(teacher, other);
	}

}
