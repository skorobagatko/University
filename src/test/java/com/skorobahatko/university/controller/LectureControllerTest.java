package com.skorobahatko.university.controller;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import static com.skorobahatko.university.util.TestUtils.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.skorobahatko.university.domain.Course;
import com.skorobahatko.university.domain.Lecture;
import com.skorobahatko.university.service.CourseService;
import com.skorobahatko.university.service.LectureService;

@SqlGroup({ 
	@Sql("/delete_tables.sql"), 
	@Sql("/create_tables.sql"), 
	@Sql("/populate_courses.sql"),
	@Sql("/populate_lectures.sql")
})
@Sql(scripts = "/delete_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {
		"file:src/test/resources/springTestContext.xml", 
		"file:src/main/webapp/WEB-INF/servletContext.xml"
		})
@WebAppConfiguration
class LectureControllerTest {
	
	MockMvc mockMvc;
	
	@Autowired
    private WebApplicationContext webApplicationContext;
	
	@Autowired
	private CourseService courseService;

	@Autowired
	private LectureService lectureService;

	@BeforeEach
	void setUp() throws Exception {
		reset(courseService);
		reset(lectureService);
		
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	void testAddLecture() throws Exception {
		Lecture lecture = getTestLectureWithCourseId(1);
		Course course = new Course(1, "Test Course", List.of(lecture));

		when(courseService.getById(1)).thenReturn(course);
		
		mockMvc.perform(post("/lectures/new", 1)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("courseId", "1")
				.param("lectureName", "Lecture 1")
				.param("lectureDate", "2020-12-01")
				.param("lectureStartTime", "08:00")
				.param("lectureEndTime", "09:00")
				.param("roomNumber", "100"))
				.andExpect(status().is(302))
				.andExpect(view().name("redirect:/courses/{id}/edit"))
				.andExpect(model().attribute("course", equalTo(course)))
				.andExpect(model().attribute("id", is(1)));

		verify(lectureService, times(1)).add(lecture);
		verifyNoMoreInteractions(lectureService);
		
		verify(courseService, times(1)).getById(1);
		verifyNoMoreInteractions(courseService);
	}

	@Test
	void testEditLecture() throws Exception {
		Lecture lecture = getTestLectureWithCourseId(1);
		
		when(lectureService.getById(0)).thenReturn(lecture);
		
		mockMvc.perform(get("/lectures/{id}/edit", 0)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isOk())
				.andExpect(view().name("lectures/edit"))
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
				.andExpect(model().attribute("course", equalTo(course)))
				.andExpect(model().attribute("id", is(1)));
		
		verify(courseService, times(1)).getById(1);
		verifyNoMoreInteractions(courseService);
	}

	@Test
	void testDeleteLectureById() throws Exception {
		Lecture lecture = getTestLectureWithCourseId(1);
		List<Lecture> lectures = new ArrayList<>();
		lectures.add(lecture);
		Course course = new Course(1, "Test Course", lectures);

		when(courseService.getById(1)).thenReturn(course);
		
		mockMvc.perform(delete("/lectures/{lectureId}", 0)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("courseId", "1"))
				.andExpect(status().is(302))
				.andExpect(view().name("redirect:/courses/{id}/edit"))
				.andDo(result -> course.removeLecture(lecture))
				.andExpect(model().attribute("course", equalTo(course)))
				.andExpect(model().attribute("id", is(1)));
		
		verify(lectureService, times(1)).removeById(0);
		verifyNoMoreInteractions(lectureService);
		
		verify(courseService, times(1)).getById(1);
		verifyNoMoreInteractions(courseService);
	}

}
