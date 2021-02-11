package com.skorobahatko.university.controller;

import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static com.skorobahatko.university.util.TestUtils.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.skorobahatko.university.domain.Course;
import com.skorobahatko.university.domain.Teacher;
import com.skorobahatko.university.service.CourseService;
import com.skorobahatko.university.service.ParticipantService;

@RunWith(SpringRunner.class)
@WebMvcTest(TeacherController.class)
class TeacherControllerTest {
	
	@MockBean
	private ParticipantService participantService;
	
	@MockBean
	private CourseService courseService;
	
	@Autowired
	MockMvc mockMvc;

	@Test
	void testGetAllTeachers() throws Exception {
		List<Teacher> teachers = getTestTeachers();
		List<Course> courses = List.of(getTestCourse());
		
		when(participantService.getAllTeachers()).thenReturn(teachers);
		when(courseService.getAll()).thenReturn(courses);
		
		mockMvc.perform(get("/teachers"))
				.andExpect(status().isOk())
				.andExpect(view().name("teachers/all"))
				.andExpect(model().attribute("teachers", equalTo(teachers)))
				.andExpect(model().attribute("courses", equalTo(courses)));
		
		verify(participantService, times(1)).getAllTeachers();
		verifyNoMoreInteractions(participantService);
	}

	@Test
	void testAddTeacher() throws Exception {
		Course course = getTestCourse();
		Teacher teacher = new Teacher("John", "Johnson");
		
		mockMvc.perform(post("/teachers/new")
				.param("firstName", teacher.getFirstName())
				.param("lastName", teacher.getLastName())
				.param("courseId", String.valueOf(course.getId())))
				.andExpect(status().is(302))
				.andExpect(view().name("redirect:/teachers"))
				.andExpect(redirectedUrl("/teachers"));
		
		verify(participantService, times(1)).add(teacher);
		verify(participantService, times(1)).addParticipantCourseById(teacher.getId(), course.getId());
		verifyNoMoreInteractions(participantService);
	}

	@Test
	void testGetTeacherById() throws Exception {
		Course course = new Course(1, "Test Course");
		Teacher teacher = new Teacher(1, "John", "Johnson", List.of(course));
		
		when(participantService.getById(1)).thenReturn(teacher);
		
		mockMvc.perform(get("/teachers/{id}", 1))
				.andExpect(status().isOk())
				.andExpect(view().name("teachers/teacher"))
				.andExpect(model().attribute("teacher", equalTo(teacher)));
		
		verify(participantService, times(1)).getById(1);
		verifyNoMoreInteractions(participantService);
	}

	@Test
	void testEditTeacherById() throws Exception {
		Course course = new Course(1, "Test Course");
		Teacher teacher = new Teacher(1, "John", "Johnson", List.of(course));
		
		when(participantService.getById(1)).thenReturn(teacher);
		
		mockMvc.perform(get("/teachers/{id}/edit", 1))
				.andExpect(status().isOk())
				.andExpect(view().name("teachers/edit"))
				.andExpect(model().attribute("teacher", equalTo(teacher)));

		verify(participantService, times(1)).getById(1);
		verifyNoMoreInteractions(participantService);
	}

	@Test
	void testAddCourseToTeacher() throws Exception {
		Course course = new Course(1, "Test Course");
		Teacher teacher = new Teacher(1, "John", "Johnson", List.of(course));
		
		when(participantService.getById(1)).thenReturn(teacher);
		
		mockMvc.perform(post("/teachers/{id}/course/add", 1)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("courseId", "1"))
				.andExpect(status().isOk())
				.andExpect(view().name("teachers/edit"));
		
		verify(participantService, times(1)).addParticipantCourseById(teacher.getId(), course.getId());
		verify(participantService, times(1)).getById(1);
		verifyNoMoreInteractions(participantService);
	}

	@Test
	void testDeleteCourseFromTeacher() throws Exception {
		Course course = new Course(1, "Test Course");
		List<Course> courses = new ArrayList<>();
		courses.add(course);
		Teacher teacher = new Teacher(1, "John", "Johnson", courses);
		
		when(participantService.getById(1)).thenReturn(teacher);
		
		mockMvc.perform(delete("/teachers/{teacherId}/course/{courseId}", 1, 1))
				.andExpect(status().isOk())
				.andExpect(view().name("teachers/edit"))
				.andDo(result -> teacher.removeCourse(course))
				.andExpect(model().attribute("teacher", hasProperty("courses", hasSize(0))));

		verify(participantService, times(1)).removeParticipantCourseById(teacher.getId(), course.getId());
		verify(participantService, times(1)).getById(1);
		verifyNoMoreInteractions(participantService);
	}

	@Test
	void testUpdateTeacherById() throws Exception {
		Teacher teacher = new Teacher(1, "John", "Johnson");
		
		mockMvc.perform(patch("/teachers/{id}", 1)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("firstName", teacher.getFirstName())
				.param("lastName", teacher.getLastName()))
				.andExpect(status().is(302))
				.andExpect(view().name("redirect:/teachers"));
		
		verify(participantService, times(1)).update(teacher);
		verifyNoMoreInteractions(participantService);
	}

	@Test
	void testDeleteTeacherById() throws Exception {
		mockMvc.perform(delete("/teachers/{id}", 1)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().is(302))
				.andExpect(view().name("redirect:/teachers"));
		
		verify(participantService, times(1)).removeById(1);
		verifyNoMoreInteractions(participantService);
	}

}
