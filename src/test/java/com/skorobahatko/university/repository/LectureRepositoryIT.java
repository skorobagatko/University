package com.skorobahatko.university.repository;

import static com.skorobahatko.university.util.TestUtils.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import com.skorobahatko.university.domain.Lecture;

@SqlGroup({ 
	@Sql("/delete_tables.sql"), 
	@Sql("/create_tables.sql"), 
	@Sql("/populate_courses.sql"),
	@Sql("/populate_lectures.sql") 
})
@DataJpaTest
class LectureRepositoryIT {
	
	@Autowired
	private LectureRepository lectureRepository;
	
	@Test
	void injectedLectureRepositoryIsNotNull() {
		assertThat(lectureRepository).isNotNull();
	}
	
	@Test
	void testFindAll() {
		int expected = 6;
		int actual = lectureRepository.findAll().size();

		assertEquals(expected, actual);
	}
	
	@Test
	void testFindByCourseId() {
		int expectedCourseId = 1;

		List<Lecture> lectures = lectureRepository.findByCourseId(expectedCourseId);

		for (Lecture lecture : lectures) {
			assertEquals(expectedCourseId, lecture.getCourseId());
		}
	}
	
	@Test
	void testFindByCourseIdForNonExistCourseId() {
		List<Lecture> expected = List.of();
		List<Lecture> actual = lectureRepository.findByCourseId(Integer.MIN_VALUE);

		assertEquals(expected, actual);
	}
	
	@Test
	void testSaveAll() {
		int lecturesInDatabase = 6;

		List<Lecture> lectures = getTestLecturesWithCourseId(1);

		lectureRepository.saveAll(lectures);

		int expected = lecturesInDatabase + lectures.size();
		int actual = lectureRepository.findAll().size();

		assertEquals(expected, actual);
	}
	
	@Test
	void testSave() {
		int lecturesInDatabase = 6;
		
		Lecture lecture = getTestLectureWithCourseId(1);
		
		lectureRepository.save(lecture);
		
		int expected = lecturesInDatabase + 1;
		int actual = lectureRepository.findAll().size();
		
		assertEquals(expected, actual);
	}

	@Test
	void testDeleteById() {
		Lecture lecture = getTestLectureWithCourseId(1);
		
		lecture = lectureRepository.save(lecture);
		
		int lectureId = lecture.getId();
		
		lectureRepository.deleteById(lectureId);
		
		Optional<Lecture> expected = Optional.empty(); 
		Optional<Lecture> actual = lectureRepository.findById(lectureId);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testDeleteByCourseId() {
		lectureRepository.deleteByCourseId(1);
		
		List<Lecture> expected = List.of();
		List<Lecture> actual = lectureRepository.findByCourseId(1);
		
		assertEquals(expected, actual);
	}

}
