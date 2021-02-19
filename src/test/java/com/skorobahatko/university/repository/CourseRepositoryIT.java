package com.skorobahatko.university.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

@SqlGroup({ 
	@Sql("/delete_tables.sql"), 
	@Sql("/create_tables.sql"), 
	@Sql("/populate_courses.sql"),
	@Sql("/populate_participants.sql")
})
@Sql(scripts = "/delete_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@DataJpaTest
class CourseRepositoryIT {

	@Autowired
	private CourseRepository courseRepository;

	@Test
	void injectedCourseRepositoryIsNotNull() {
		assertThat(courseRepository).isNotNull();
	}
	
	@Test
	void exceptionIsThrowsIfIdIsNotValid() {
		assertThrows(DataAccessException.class, () -> courseRepository.findById(null));
	}

}
