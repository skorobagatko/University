package com.skorobahatko.university.controller;

import static com.skorobahatko.university.util.TestUtils.getTestTeacher;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.skorobahatko.university.domain.Teacher;
import com.skorobahatko.university.service.CourseService;
import com.skorobahatko.university.service.TeacherService;

@RunWith(MockitoJUnitRunner.class)
class TeacherControllerTest {

	private TeacherController teacherController;
	private TeacherService teacherService;
	private CourseService courseService;
	
	@BeforeEach
	public void init() {
		teacherService = Mockito.mock(TeacherService.class);
		courseService = Mockito.mock(CourseService.class);
		teacherController = new TeacherController(teacherService, courseService);
	}
	
	@Test
	void allTeachersAreAddedToModelForTeachersListView() {
		Model model = new ExtendedModelMap();
		Teacher teacher = getTestTeacher();
		List<Teacher> teachers = List.of(teacher);
		
		when(teacherService.getAll()).thenReturn(teachers);
		
		assertThat(teacherController.getAllTeachers(model), equalTo("teachers/all"));
		assertThat(model.asMap(), hasEntry("teachers", teachers));
	}
	
	@Test
	void redirectIsSuccessfulForNewAddedTeacher() {
		String firstName = "Test";
		String lastName = "Teacher";
		int courseId = 1;
		
		Teacher teacher = getTestTeacher();
		when(teacherService.add(any())).thenReturn(teacher);
		
		assertThat(teacherController.addTeacher(
				firstName,
				lastName,
				courseId), equalTo("redirect:/teachers"));
	}
	
	@Test
	void teacherWithRequestedIdIsAddedToModel() {
		Model model = new ExtendedModelMap();
		
		Teacher teacher = getTestTeacher();
		when(teacherService.getById(0)).thenReturn(teacher);
		
		assertThat(teacherController.getTeacherById(0, model), equalTo("teachers/teacher"));
		assertThat(model.asMap(), hasEntry("teacher", teacher));
	}
	
	@Test
	void editTeacherViewIsOpenedForTeacherWithRequestedId() {
		Model model = new ExtendedModelMap();
		
		Teacher teacher = getTestTeacher();
		when(teacherService.getById(0)).thenReturn(teacher);
		
		assertThat(teacherController.editTeacherById(0, model), equalTo("teachers/edit"));
		assertThat(model.asMap(), hasEntry("teacher", teacher));
	}
	
	@Test
	void redirectIsSuccessfulForUpdatedTeacher() {
		Teacher teacher = getTestTeacher();
		BindingResult bindingResult = Mockito.mock(BindingResult.class);
		
		assertThat(teacherController.updateTeacher(teacher, bindingResult), equalTo("redirect:/teachers"));
	}
	
	@Test
	void teacherEditViewIsOpenedIfUserInputErrorsWasFoundWhileUpdatingTeacher() {
		Teacher teacher = getTestTeacher();
		BindingResult bindingResult = Mockito.mock(BindingResult.class);
		when(bindingResult.hasErrors()).thenReturn(true);
		
		assertThat(teacherController.updateTeacher(teacher, bindingResult), equalTo("teachers/edit"));
	}
	
	@Test
	void teacherEditViewIsOpenedAfterAddingCourseToTeacher() {
		int teacherId = 1;
		int courseId = 1;
		Model model = new ExtendedModelMap();
		
		Teacher teacher = new Teacher(1, "Test", "Teacher");
		when(teacherService.getById(1)).thenReturn(teacher);
		
		assertThat(teacherController.addTeacherCourse(teacherId, courseId, model), equalTo("teachers/edit"));
		assertThat(model.asMap(), hasEntry("teacher", teacher));
	}
	
	@Test
	void teacherEditViewIsOpenedAfterDeletingTeacherCourse() {
		int teacherId = 1;
		int courseId = 1;
		Model model = new ExtendedModelMap();
		
		Teacher teacher = new Teacher(1, "Test", "Teacher");
		when(teacherService.getById(1)).thenReturn(teacher);
		
		assertThat(teacherController.deleteTeacherCourse(teacherId, courseId, model), equalTo("teachers/edit"));
		assertThat(model.asMap(), hasEntry("teacher", teacher));
	}
	
	@Test
	void redirectIsSuccessfulAfterDeletingTeacher() {
		assertThat(teacherController.deleteTeacherById(1), equalTo("redirect:/teachers"));
	}
	
}
