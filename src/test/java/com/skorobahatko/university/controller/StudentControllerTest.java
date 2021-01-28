package com.skorobahatko.university.controller;

import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static com.skorobahatko.university.util.TestUtils.*;

import java.util.ArrayList;
import java.util.List;

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
import com.skorobahatko.university.domain.Student;
import com.skorobahatko.university.service.CourseService;
import com.skorobahatko.university.service.ParticipantService;

@SqlGroup({ 
	@Sql("/delete_tables.sql"), 
	@Sql("/create_tables.sql"), 
	@Sql("/populate_courses.sql"),
	@Sql("/populate_participants.sql")
})
@Sql(scripts = "/delete_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {
		"file:src/test/resources/springTestContext.xml", 
		"file:src/main/webapp/WEB-INF/servletContext.xml"
		})
@WebAppConfiguration
class StudentControllerTest {
	
	MockMvc mockMvc;
	
	@Autowired
    private WebApplicationContext webApplicationContext;
	
	@Autowired
	private ParticipantService participantService;
	
	@Autowired
	private CourseService courseService;

	@BeforeEach
	void setUp() throws Exception {
		reset(courseService);
		reset(participantService);
		
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	void testGetAllStudents() throws Exception {
		List<Student> students = getTestStudents();
		
		when(participantService.getAllStudents()).thenReturn(students);
		
		mockMvc.perform(get("/students"))
				.andExpect(status().isOk())
				.andExpect(view().name("students/all"))
				.andExpect(model().attribute("students", equalTo(students)));
		
		verify(participantService, times(1)).getAllStudents();
		verifyNoMoreInteractions(participantService);
	}

	@Test
	void testAddStudent() throws Exception {
		Course course = getTestCourse();
		Student student = new Student("John", "Johnson");
		
		mockMvc.perform(post("/students/new")
				.param("firstName", student.getFirstName())
				.param("lastName", student.getLastName())
				.param("courseId", String.valueOf(course.getId())))
				.andExpect(status().is(302))
				.andExpect(view().name("redirect:/students"))
				.andExpect(redirectedUrl("/students"));
		
		verify(participantService, times(1)).add(student);
		verify(participantService, times(1)).addParticipantCourseById(student.getId(), course.getId());
		verifyNoMoreInteractions(participantService);
	}

	@Test
	void testGetStudentById() throws Exception {
		Course course = new Course(1, "Test Course");
		Student student = new Student(1, "John", "Johnson", List.of(course));
		
		when(participantService.getById(1)).thenReturn(student);
		
		mockMvc.perform(get("/students/{id}", 1))
				.andExpect(status().isOk())
				.andExpect(view().name("students/student"))
				.andExpect(model().attribute("student", equalTo(student)));
		
		verify(participantService, times(1)).getById(1);
		verifyNoMoreInteractions(participantService);
	}

	@Test
	void testEditStudentById() throws Exception {
		Course course = new Course(1, "Test Course");
		Student student = new Student(1, "John", "Johnson", List.of(course));
		
		when(participantService.getById(1)).thenReturn(student);
		
		mockMvc.perform(get("/students/{id}/edit", 1))
				.andExpect(status().isOk())
				.andExpect(view().name("students/edit"))
				.andExpect(model().attribute("student", equalTo(student)));
		
		verify(participantService, times(1)).getById(1);
		verifyNoMoreInteractions(participantService);
	}

	@Test
	void testAddCourseToStudent() throws Exception {
		Course course = new Course(1, "Test Course");
		Student student = new Student(1, "John", "Johnson", List.of(course));
		
		when(participantService.getById(1)).thenReturn(student);
		
		mockMvc.perform(post("/students/{id}/course/add", 1)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("courseId", "1"))
				.andExpect(status().isOk())
				.andExpect(view().name("students/edit"));
		
		verify(participantService, times(1)).addParticipantCourseById(student.getId(), course.getId());
		verify(participantService, times(1)).getById(1);
		verifyNoMoreInteractions(participantService);
	}

	@Test
	void testDeleteCourseFromStudent() throws Exception {
		Course course = new Course(1, "Test Course");
		List<Course> courses = new ArrayList<>();
		courses.add(course);
		Student student = new Student(1, "John", "Johnson", courses);
		
		when(participantService.getById(1)).thenReturn(student);
		
		mockMvc.perform(delete("/students/{studentId}/course/{courseId}", 1, 1))
				.andExpect(status().isOk())
				.andExpect(view().name("students/edit"))
				.andDo(result -> student.removeCourse(course))
				.andExpect(model().attribute("student", hasProperty("courses", hasSize(0))));

		verify(participantService, times(1)).removeParticipantCourseById(student.getId(), course.getId());
		verify(participantService, times(1)).getById(1);
		verifyNoMoreInteractions(participantService);
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
		
		verify(participantService, times(1)).update(student);
		verifyNoMoreInteractions(participantService);
	}

	@Test
	void testDeleteStudentById() throws Exception {
		mockMvc.perform(delete("/students/{id}", 1)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().is(302))
				.andExpect(view().name("redirect:/students"));
		
		verify(participantService, times(1)).removeById(1);
		verifyNoMoreInteractions(participantService);
	}

}
