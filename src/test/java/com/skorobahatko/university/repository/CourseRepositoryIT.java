package com.skorobahatko.university.repository;

import static com.skorobahatko.university.util.TestUtils.getTestCourse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import com.skorobahatko.university.domain.Course;

@SqlGroup({
	@Sql("/delete_tables.sql"), 
	@Sql("/create_tables.sql"), 
	@Sql("/populate_courses.sql"),
	@Sql("/populate_participants.sql")
})
@DataJpaTest
class CourseRepositoryIT {

	@Autowired
	private CourseRepository courseRepository;

	@Test
	void injectedCourseRepositoryIsNotNull() {
		assertThat(courseRepository).isNotNull();
	}
	
	@Test
	void exceptionIsThrownForNotValidId() {
		assertThrows(DataAccessException.class, () -> courseRepository.findById(null));
	}
	
	@Test
	void testFindAll() {
		int expected = 3;
		int actual = courseRepository.findAll().size();

		assertEquals(expected, actual);
	}
	
	@Test
	void testFindById() {
		Course course = getTestCourse();
		course = courseRepository.save(course);
        int expectedId = course.getId();

        Optional<Course> courseOptional = courseRepository.findById(expectedId);
        assertTrue(courseOptional.isPresent());

        int actualId = courseOptional.get().getId();
        assertEquals(expectedId, actualId);
	}
	
	@Test
	void testSave() {
		int coursesInDatabase = (int) courseRepository.count();
		Course course = getTestCourse();
		course = courseRepository.save(course);
		
		int expectedCoursesInDatabase = coursesInDatabase + 1;
		int actualCoursesInDatabase = (int) courseRepository.count();
		assertEquals(expectedCoursesInDatabase, actualCoursesInDatabase);
		
		assertEquals(course, courseRepository.findById(course.getId()).get());
	}
	
	@Test
	void testDeleteById() {
		Course course = getTestCourse();
		course = courseRepository.save(course);
        int courseId = course.getId();
        
        courseRepository.deleteById(courseId);
        
        Optional<Course> expected = Optional.empty();
        Optional<Course> actual = courseRepository.findById(courseId);

        assertEquals(expected, actual);
	}

}
