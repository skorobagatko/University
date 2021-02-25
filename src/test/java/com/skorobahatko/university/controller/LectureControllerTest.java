package com.skorobahatko.university.controller;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import static com.skorobahatko.university.util.TestUtils.*;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.skorobahatko.university.domain.Course;
import com.skorobahatko.university.domain.Lecture;
import com.skorobahatko.university.service.CourseService;
import com.skorobahatko.university.service.LectureService;

@RunWith(SpringRunner.class)
@WebMvcTest(LectureController.class)
class LectureControllerTest {

	@MockBean
	private CourseService courseService;

	@MockBean
	private LectureService lectureService;

	@Autowired
	MockMvc mockMvc;

	@Test
	void testAddLecture() throws Exception {
		Lecture lecture = getTestLectureWithCourseId(1);
		Course course = new Course(1, "Test Course", List.of(lecture));

		when(courseService.getById(1)).thenReturn(course);

		mockMvc.perform(post("/lectures/new").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("courseId", "1")
				.param("lectureName", "Lecture 1").param("lectureDate", "2020-12-01").param("lectureStartTime", "08:00")
				.param("lectureEndTime", "09:00").param("roomNumber", "100")).andExpect(status().is(302))
				.andExpect(view().name("redirect:/courses/{id}/edit"))
				.andExpect(model().attribute("course", course.toString())).andExpect(model().attribute("id", is("1")));

		verify(lectureService, times(1)).add(lecture);
		verifyNoMoreInteractions(lectureService);

		verify(courseService, times(1)).getById(1);
		verifyNoMoreInteractions(courseService);
	}

	@Test
	void testEditLecture() throws Exception {
		Lecture lecture = getTestLectureWithCourseId(1);

		when(lectureService.getById(0)).thenReturn(lecture);

		mockMvc.perform(get("/lectures/{id}/edit", 0).contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isOk()).andExpect(view().name("lectures/edit"))
				.andExpect(model().attribute("lecture", equalTo(lecture)));

		verify(lectureService, times(1)).getById(0);
		verifyNoMoreInteractions(lectureService);
	}

	@Test
	void testUpdateLecture() throws Exception {
		Lecture lecture = getTestLectureWithCourseId(1);
		Course course = new Course(1, "Test Course", List.of(lecture));

		when(courseService.getById(1)).thenReturn(course);

		mockMvc.perform(patch("/lectures/{id}", 0)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.requestAttr("lecture", lecture)
				.param("courseId", "1")
				.param("name", "Lecture 1")
				.param("date", "2020-12-01")
				.param("startTime", "08:00")
				.param("endTime", "09:00")
				.param("roomNumber", "100"))
				.andExpect(status().is(302))
				.andExpect(view().name("redirect:/courses/{id}/edit"))
				.andExpect(model().attribute("course", course.toString()))
				.andExpect(model().attribute("id", is("1")));

		verify(courseService, times(1)).getById(1);
		verifyNoMoreInteractions(courseService);
	}
	
	@Test
	void testUpdateLectureValidationWithEmptyName() throws Exception {
		mockMvc.perform(patch("/lectures/{id}", 0)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("name", ""))
				.andExpect(status().isOk())
				.andExpect(view().name("lectures/edit"))
				.andExpect(model().attributeHasFieldErrors("lecture", "name"));
	}
	
	@Test
	void testUpdateLectureValidationWithShortName() throws Exception {
		mockMvc.perform(patch("/lectures/{id}", 0)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("name", "L"))
				.andExpect(status().isOk())
				.andExpect(view().name("lectures/edit"))
				.andExpect(model().attributeHasFieldErrors("lecture", "name"));
	}
	
	@Test
	void testUpdateLectureValidationWithEmptyDate() throws Exception {
		mockMvc.perform(patch("/lectures/{id}", 0)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("date", ""))
				.andExpect(status().isOk())
				.andExpect(view().name("lectures/edit"))
				.andExpect(model().attributeHasFieldErrors("lecture", "date"));
	}
	
	@Test
	void testUpdateLectureValidationWithEmptyStartTime() throws Exception {
		mockMvc.perform(patch("/lectures/{id}", 0)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("startTime", ""))
				.andExpect(status().isOk())
				.andExpect(view().name("lectures/edit"))
				.andExpect(model().attributeHasFieldErrors("lecture", "startTime"));
	}
	
	@Test
	void testUpdateLectureValidationWithEmptyEndTime() throws Exception {
		mockMvc.perform(patch("/lectures/{id}", 0)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("endTime", ""))
				.andExpect(status().isOk())
				.andExpect(view().name("lectures/edit"))
				.andExpect(model().attributeHasFieldErrors("lecture", "endTime"));
	}
	
	@Test
	void testUpdateLectureValidationWithEmptyRoomNumber() throws Exception {
		mockMvc.perform(patch("/lectures/{id}", 0)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("roomNumber", ""))
				.andExpect(status().isOk())
				.andExpect(view().name("lectures/edit"))
				.andExpect(model().attributeHasFieldErrors("lecture", "roomNumber"));
	}

	@Test
	void testDeleteLectureById() throws Exception {
		Lecture lecture = getTestLectureWithCourseId(1);
		List<Lecture> lectures = new ArrayList<>();
		lectures.add(lecture);
		Course course = new Course(1, "Test Course", lectures);

		when(courseService.getById(1)).thenReturn(course);
		doAnswer((invocation) -> {
			course.removeLecture(lecture);
			return null;
		}).when(lectureService).removeById(0);

		mockMvc.perform(delete("/lectures/{lectureId}", 0).contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("courseId", "1"))
				.andExpect(status().is(302))
				.andExpect(view().name("redirect:/courses/{id}/edit"))
				.andExpect(model().attribute("course", course.toString()))
				.andExpect(model().attribute("id", is("1")));

		verify(lectureService, times(1)).removeById(0);
		verifyNoMoreInteractions(lectureService);

		verify(courseService, times(1)).getById(1);
		verifyNoMoreInteractions(courseService);
	}

}
