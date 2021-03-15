package com.skorobahatko.university.controller;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.MatcherAssert.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import static com.skorobahatko.university.util.TestUtils.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.skorobahatko.university.domain.Course;
import com.skorobahatko.university.service.CourseService;

@RunWith(MockitoJUnitRunner.class)
class CourseControllerTest {
	
	private CourseController courseController;
	private CourseService courseService;
	private MockMvc mockMvc;
	
	@BeforeEach
	public void init() {
		courseService = Mockito.mock(CourseService.class);
		courseController = new CourseController(courseService);
		mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();
	}
	
	@Test
	void allCoursesAreAddedToModelForCoursesListView() throws Exception {
		Model model = new ExtendedModelMap();
		Course course = getTestCourse();
		List<Course> courses = List.of(course);
		
		when(courseService.getAll()).thenReturn(courses);
		
		assertThat(courseController.getAllCourses(model), equalTo("courses/all"));
		assertThat(model.asMap(), hasEntry("courses", courses));
	}
	
	@Test
	void requestForCoursesIsSuccessfullyProcessedWithAvailableCoursesList() throws Exception {
		Course course = getTestCourse();
		List<Course> courses = List.of(course);
		
		when(courseService.getAll()).thenReturn(courses);
		
		mockMvc.perform(get("/courses"))
				.andExpect(status().isOk())
				.andExpect(model().attribute("courses", equalTo(courses)))
				.andExpect(forwardedUrl("courses/all"));
	}
	
	@Test
	void redirectIsSuccessfulForNewAddedCourse() {
		String courseName = "Test Course";
		RedirectAttributes attributes = Mockito.mock(RedirectAttributes.class);
		
		Course course = new Course(1, courseName);
		when(courseService.add(any())).thenReturn(course);
		
		assertThat(courseController.addCourse(courseName, attributes), equalTo("redirect:/courses/{id}"));
	}
	
	@Test
	void courseWithRequestedIdIsAddedToModel() {
		Model model = new ExtendedModelMap();
		
		Course course = new Course(1, "Test Course");
		when(courseService.getById(1)).thenReturn(course);
		
		assertThat(courseController.getCourseById(1, model), equalTo("courses/course"));
		assertThat(model.asMap(), hasEntry("course", course));
	}
	
	@Test
	void editCourseViewIsOpenedForCourseWithRequestedId() {
		Model model = new ExtendedModelMap();
		
		Course course = new Course(1, "Test Course");
		when(courseService.getById(1)).thenReturn(course);
		
		assertThat(courseController.editCourseById(1, model), equalTo("courses/edit"));
		assertThat(model.asMap(), hasEntry("course", course));
	}
	
	@Test
	void redirectIsSuccessfulForUpdatedCourse() {
		Course course = getTestCourse();
		BindingResult bindingResult = Mockito.mock(BindingResult.class);
		
		assertThat(courseController.updateCourse(course, bindingResult), equalTo("redirect:/courses"));
	}
	
	@Test
	void courseEditViewIsOpenedIfUserInputErrorsWasFoundWhileUpdatingCourse() {
		Course course = getTestCourse();
		BindingResult bindingResult = Mockito.mock(BindingResult.class);
		when(bindingResult.hasErrors()).thenReturn(true);
		
		assertThat(courseController.updateCourse(course, bindingResult), equalTo("courses/edit"));
	}
	
	@Test
	void redirectIsSuccessfulAfterDeletingCourse() {
		assertThat(courseController.deleteCourseById(1), equalTo("redirect:/courses"));
	}
	
}
