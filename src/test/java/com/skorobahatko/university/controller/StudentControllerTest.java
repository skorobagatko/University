package com.skorobahatko.university.controller;

import com.skorobahatko.university.domain.Course;
import com.skorobahatko.university.domain.Student;
import com.skorobahatko.university.service.CourseService;
import com.skorobahatko.university.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static com.skorobahatko.university.util.TestUtils.getTestCourse;
import static com.skorobahatko.university.util.TestUtils.getTestStudents;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(StudentController.class)
class StudentControllerTest {
	
	@MockBean
	private StudentService studentService;
	
	@MockBean
	private CourseService courseService;
	
	@Autowired
	MockMvc mockMvc;

	@Test
	void testGetAllStudents() throws Exception {
		List<Student> students = getTestStudents();
		List<Course> courses = List.of(getTestCourse());

		when(studentService.getAll()).thenReturn(students);
		when(courseService.getAll()).thenReturn(courses);

		mockMvc.perform(get("/students"))
				.andExpect(status().isOk())
				.andExpect(view().name("students/all"))
				.andExpect(model().attribute("students", equalTo(students)))
				.andExpect(model().attribute("courses", equalTo(courses)));

		verify(studentService, times(1)).getAll();
		verifyNoMoreInteractions(studentService);

		verify(courseService, times(1)).getAll();
		verifyNoMoreInteractions(courseService);
	}

	@Test
	void testAddStudent() throws Exception {
		Course course = getTestCourse();
		Student student = new Student("John", "Johnson");

		when(studentService.add(student)).thenReturn(student);

		mockMvc.perform(post("/students/new")
				.param("firstName", student.getFirstName())
				.param("lastName", student.getLastName())
				.param("courseId", String.valueOf(course.getId())))
				.andExpect(status().is(302))
				.andExpect(view().name("redirect:/students"))
				.andExpect(redirectedUrl("/students"));

		verify(studentService, times(1)).add(student);
		verify(studentService, times(1)).addCourseToStudentById(student.getId(), course.getId());
		verifyNoMoreInteractions(studentService);
	}

	@Test
	void testGetStudentById() throws Exception {
		Course course = new Course(1, "Test Course");
		Student student = new Student(1, "John", "Johnson", List.of(course));

		when(studentService.getById(1)).thenReturn(student);

		mockMvc.perform(get("/students/{id}", 1))
				.andExpect(status().isOk())
				.andExpect(view().name("students/student"))
				.andExpect(model().attribute("student", equalTo(student)));

		verify(studentService, times(1)).getById(1);
		verifyNoMoreInteractions(studentService);
	}

	@Test
	void testEditStudentById() throws Exception {
		Course course = new Course(1, "Test Course");
		Student student = new Student(1, "John", "Johnson", List.of(course));

		when(studentService.getById(1)).thenReturn(student);

		mockMvc.perform(get("/students/{id}/edit", 1))
				.andExpect(status().isOk())
				.andExpect(view().name("students/edit"))
				.andExpect(model().attribute("student", equalTo(student)));

		verify(studentService, times(1)).getById(1);
		verifyNoMoreInteractions(studentService);
	}

	@Test
	void testAddCourseToStudent() throws Exception {
		Course course = new Course(1, "Test Course");
		Student student = new Student(1, "John", "Johnson", List.of(course));

		when(studentService.getById(1)).thenReturn(student);

		mockMvc.perform(post("/students/{id}/course/add", 1)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("courseId", "1"))
				.andExpect(status().isOk())
				.andExpect(view().name("students/edit"));

		verify(studentService, times(1)).addCourseToStudentById(student.getId(), course.getId());
		verify(studentService, times(1)).getById(1);
		verifyNoMoreInteractions(studentService);
	}

	@Test
	void testDeleteCourseFromStudent() throws Exception {
		Course course = new Course(1, "Test Course");
		List<Course> courses = new ArrayList<>();
		courses.add(course);
		Student student = new Student(1, "John", "Johnson", courses);

		when(studentService.getById(1)).thenReturn(student);

		mockMvc.perform(delete("/students/{studentId}/course/{courseId}", 1, 1))
				.andExpect(status().isOk())
				.andExpect(view().name("students/edit"))
				.andDo(result -> student.removeCourse(course))
				.andExpect(model().attribute("student", hasProperty("courses", hasSize(0))));

		verify(studentService, times(1)).deleteStudentsCourseById(student.getId(), course.getId());
		verify(studentService, times(1)).getById(1);
		verifyNoMoreInteractions(studentService);
	}

	@Test
	void testUpdateStudentById() throws Exception {
		Student student = new Student(1, "John", "Johnson");

		mockMvc.perform(patch("/students/{id}", 1)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("firstName", student.getFirstName())
				.param("lastName", student.getLastName()))
				.andExpect(status().is(302))
				.andExpect(view().name("redirect:/students"));

		verify(studentService, times(1)).update(student);
		verifyNoMoreInteractions(studentService);
	}

	@Test
	void testUpdateStudentValidationWithEmptyFirstName() throws Exception {
		mockMvc.perform(patch("/students/{id}", 1)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("firstName", ""))
				.andExpect(status().isOk())
				.andExpect(view().name("students/edit"))
				.andExpect(model().attributeHasFieldErrors("student", "firstName"));
	}

	@Test
	void testUpdateStudentValidationWithShortFirstName() throws Exception {
		mockMvc.perform(patch("/students/{id}", 1)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("firstName", "S"))
				.andExpect(status().isOk())
				.andExpect(view().name("students/edit"))
				.andExpect(model().attributeHasFieldErrors("student", "firstName"));
	}

	@Test
	void testUpdateStudentValidationWithEmptyLastName() throws Exception {
		mockMvc.perform(patch("/students/{id}", 1)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("lastName", ""))
				.andExpect(status().isOk())
				.andExpect(view().name("students/edit"))
				.andExpect(model().attributeHasFieldErrors("student", "lastName"));
	}

	@Test
	void testUpdateStudentValidationWithShortLastName() throws Exception {
		mockMvc.perform(patch("/students/{id}", 1)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("lastName", "S"))
				.andExpect(status().isOk())
				.andExpect(view().name("students/edit"))
				.andExpect(model().attributeHasFieldErrors("student", "lastName"));
	}

	@Test
	void testDeleteStudentById() throws Exception {
		mockMvc.perform(delete("/students/{id}", 1)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().is(302))
				.andExpect(view().name("redirect:/students"));

		verify(studentService, times(1)).removeById(1);
		verifyNoMoreInteractions(studentService);
	}

}
