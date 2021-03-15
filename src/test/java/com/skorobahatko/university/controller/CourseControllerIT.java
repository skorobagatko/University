package com.skorobahatko.university.controller;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static com.skorobahatko.university.util.TestUtils.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.junit4.SpringRunner;

import com.skorobahatko.university.domain.Course;
import com.skorobahatko.university.domain.Lecture;
import com.skorobahatko.university.service.CourseService;

@RunWith(SpringRunner.class)
@WebMvcTest(CourseController.class)
class CourseControllerIT {
	
	@MockBean
	private CourseService courseService;

	@Autowired
	MockMvc mockMvc;

	@Test
	void testGetAllCourses() throws Exception {
		Course course = new Course(1, "Test Course");
		List<Course> courses = List.of(course);
		
		when(courseService.getAll()).thenReturn(courses);
		
		mockMvc.perform(get("/courses"))
				.andExpect(status().isOk())
				.andExpect(view().name("courses/all"))
				.andExpect(model().attribute("courses", hasSize(1)))
				.andExpect(model().attribute("courses", hasItem(
                        allOf(
                        		hasProperty("id", is(1)),
                        		hasProperty("name", is("Test Course"))
                        )
                )));
		
		verify(courseService, times(1)).getAll();
		verifyNoMoreInteractions(courseService);
	}

	@Test
	void testAddCourse() throws Exception {
		Course course = new Course("Test Course");
		
		when(courseService.add(course)).thenReturn(course);

		mockMvc.perform(post("/courses/new")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("courseName", "Test Course"))
				.andExpect(status().is(302))
				.andExpect(view().name("redirect:/courses/{id}"))
				.andExpect(redirectedUrl("/courses/0"))
				.andExpect(model().attribute("id", is("0")));
		
		verify(courseService, times(1)).add(course);
		verifyNoMoreInteractions(courseService);
	}

	@Test
	void testGetCourseById() throws Exception {
		List<Lecture> lectures = getTestLecturesWithCourseId(1);
		Course course = new Course(1, "Test Course", lectures);
		
		when(courseService.getById(1)).thenReturn(course);
		
		mockMvc.perform(get("/courses/{id}", 1))
				.andExpect(status().isOk())
				.andExpect(view().name("courses/course"))
				.andExpect(model().attribute("course", hasProperty("id", is(1))))
				.andExpect(model().attribute("course", hasProperty("name", is("Test Course"))))
				.andExpect(model().attribute("course", hasProperty("lectures", equalTo(lectures))));
		
		verify(courseService, times(1)).getById(1);
		verifyNoMoreInteractions(courseService);
	}

	@Test
	void testEditCourseById() throws Exception {
		List<Lecture> lectures = getTestLecturesWithCourseId(1);
		Course course = new Course(1, "Test Course", lectures);
		
		when(courseService.getById(1)).thenReturn(course);
		
		mockMvc.perform(get("/courses/{id}/edit", 1))
				.andExpect(status().isOk())
				.andExpect(view().name("courses/edit"))
				.andExpect(model().attribute("course", hasProperty("id", is(1))))
				.andExpect(model().attribute("course", hasProperty("name", is("Test Course"))))
				.andExpect(model().attribute("course", hasProperty("lectures", is(lectures))));

		verify(courseService, times(1)).getById(1);
		verifyNoMoreInteractions(courseService);
	}

	@Test
	void testUpdateCourseById() throws Exception {
		Lecture lecture = getTestLectureWithCourseId(1);
		Course course = new Course(1, "Test Course", List.of(lecture));
			
		mockMvc.perform(patch("/courses/{id}", 1)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.requestAttr("course", course)
				.param("name", "Test Course")
				.param("lectures[0].id", "0")
				.param("lectures[0].courseId", "1")
				.param("lectures[0].name", "Lecture 1")
				.param("lectures[0].date", "2020-12-01")
				.param("lectures[0].startTime", "08:00")
				.param("lectures[0].endTime", "09:00")
				.param("lectures[0].roomNumber", "100"))
				.andExpect(status().is(302))
				.andExpect(view().name("redirect:/courses"));
		
		verify(courseService, times(1)).update(course);
		verifyNoMoreInteractions(courseService);
	}
	
	@Test
	void testUpdateCourseValidationWithEmptyName() throws Exception {
		mockMvc.perform(patch("/courses/{id}", 1)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("name", ""))
				.andExpect(status().isOk())
				.andExpect(view().name("courses/edit"))
				.andExpect(model().attributeHasFieldErrors("course", "name"));
	}
	
	@Test
	void testUpdateCourseValidationWithShortName() throws Exception {
		mockMvc.perform(patch("/courses/{id}", 1)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("name", "C"))
				.andExpect(status().isOk())
				.andExpect(view().name("courses/edit"))
				.andExpect(model().attributeHasFieldErrors("course", "name"));
	}

	@Test
	void testDeleteCourseById() throws Exception {
		mockMvc.perform(delete("/courses/{id}", 1)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().is(302))
				.andExpect(view().name("redirect:/courses"));
		
		verify(courseService, times(1)).removeById(1);
		verifyNoMoreInteractions(courseService);
	}

}
