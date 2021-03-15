package com.skorobahatko.university.controller;

import static com.skorobahatko.university.util.TestUtils.getTestStudent;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.skorobahatko.university.domain.Student;
import com.skorobahatko.university.service.CourseService;
import com.skorobahatko.university.service.StudentService;

@RunWith(MockitoJUnitRunner.class)
class StudentControllerTest {
	
	private StudentController studentController;
	private StudentService studentService;
	private CourseService courseService;
	
	@BeforeEach
	public void init() {
		studentService = Mockito.mock(StudentService.class);
		courseService = Mockito.mock(CourseService.class);
		studentController = new StudentController(studentService, courseService);
	}
	
	@Test
	void allStudentsAreAddedToModelForStudentsListView() throws Exception {
		Model model = new ExtendedModelMap();
		Student student = getTestStudent();
		List<Student> students = List.of(student);
		
		when(studentService.getAll()).thenReturn(students);
		
		assertThat(studentController.getAllStudents(model), equalTo("students/all"));
		assertThat(model.asMap(), hasEntry("students", students));
	}
	
	@Test
	void redirectIsSuccessfulForNewAddedStudent() {
		String firstName = "Test";
		String lastName = "Student";
		int courseId = 1;
		
		Student student = getTestStudent();
		when(studentService.add(any())).thenReturn(student);
		
		assertThat(studentController.addStudent(
				firstName,
				lastName,
				courseId), equalTo("redirect:/students"));
	}
	
	@Test
	void studentWithRequestedIdIsAddedToModel() {
		Model model = new ExtendedModelMap();
		
		Student student = getTestStudent();
		when(studentService.getById(0)).thenReturn(student);
		
		assertThat(studentController.getStudentById(0, model), equalTo("students/student"));
		assertThat(model.asMap(), hasEntry("student", student));
	}
	
	@Test
	void editStudentViewIsOpenedForStudentWithRequestedId() {
		Model model = new ExtendedModelMap();
		
		Student student = getTestStudent();
		when(studentService.getById(0)).thenReturn(student);
		
		assertThat(studentController.editStudentById(0, model), equalTo("students/edit"));
		assertThat(model.asMap(), hasEntry("student", student));
	}
	
	@Test
	void redirectIsSuccessfulForUpdatedStudent() {
		Student student = getTestStudent();
		BindingResult bindingResult = Mockito.mock(BindingResult.class);
		
		assertThat(studentController.updateStudent(student, bindingResult), equalTo("redirect:/students"));
	}
	
	@Test
	void studentEditViewIsOpenedIfUserInputErrorsWasFoundWhileUpdatingStudent() {
		Student student = getTestStudent();
		BindingResult bindingResult = Mockito.mock(BindingResult.class);
		when(bindingResult.hasErrors()).thenReturn(true);
		
		assertThat(studentController.updateStudent(student, bindingResult), equalTo("students/edit"));
	}
	
	@Test
	void studentEditViewIsOpenedAfterAddingCourseToStudent() {
		int studentId = 1;
		int courseId = 1;
		Model model = new ExtendedModelMap();
		
		Student student = new Student(1, "Test", "Student");
		when(studentService.getById(1)).thenReturn(student);
		
		assertThat(studentController.addStudentCourse(studentId, courseId, model), equalTo("students/edit"));
		assertThat(model.asMap(), hasEntry("student", student));
	}
	
	@Test
	void studentEditViewIsOpenedAfterDeletingStudentCourse() {
		int studentId = 1;
		int courseId = 1;
		Model model = new ExtendedModelMap();
		
		Student student = new Student(1, "Test", "Student");
		when(studentService.getById(1)).thenReturn(student);
		
		assertThat(studentController.deleteStudentCourse(studentId, courseId, model), equalTo("students/edit"));
		assertThat(model.asMap(), hasEntry("student", student));
	}
	
	@Test
	void redirectIsSuccessfulAfterDeletingStudent() {
		assertThat(studentController.deleteStudentById(1), equalTo("redirect:/students"));
	}

}
