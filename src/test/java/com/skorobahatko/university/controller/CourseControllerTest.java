package com.skorobahatko.university.controller;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static com.skorobahatko.university.util.TestUtils.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.skorobahatko.university.domain.Course;
import com.skorobahatko.university.domain.Lecture;
import com.skorobahatko.university.service.CourseService;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

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
class CourseControllerTest {

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

		mockMvc.perform(post("/courses/new")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("courseName", "Test Course"))
				.andExpect(status().is(302))
				.andExpect(view().name("redirect:/courses/{id}"))
				.andExpect(redirectedUrl("/courses/0"))
				.andExpect(model().attribute("id", is(0)));
		
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
				.param("name", "Test Course")
				.param("lectures[0].id", "0")
				.param("lectures[0].courseId", "1")
				.param("lectures[0].name", "Lecture 1")
				.param("lectures[0].date", "12/01/20")
				.param("lectures[0].startTime", "7:30 AM")
				.param("lectures[0].endTime", "9:00 AM")
				.param("lectures[0].roomNumber", "100"))
				.andExpect(status().is(302))
				.andExpect(view().name("redirect:/courses"))
				.andExpect(model().attribute("course", equalTo(course)));
		
		verify(courseService, times(1)).update(course);
		verifyNoMoreInteractions(courseService);
	}

	@Test
	void testAddNewLecture() throws Exception {
		Lecture lecture = getTestLectureWithCourseId(1);
		Course course = new Course(1, "Test Course", List.of(lecture));

		when(courseService.getById(1)).thenReturn(course);
		
		mockMvc.perform(put("/courses/{courseId}/lecture/new", 1)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("lectureName", "Lecture 1")
				.param("lectureDate", "2020-12-01")
				.param("lectureStartTime", "07:30")
				.param("lectureEndTime", "09:00")
				.param("roomNumber", "100"))
				.andExpect(status().isOk())
				.andExpect(view().name("courses/edit"))
				.andExpect(model().attribute("course", equalTo(course)));

		verify(lectureService, times(1)).add(lecture);
		verifyNoMoreInteractions(lectureService);
		
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
		
		mockMvc.perform(delete("/courses/{courseId}/lecture/{lectureId}", 1, 1)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isOk())
				.andExpect(view().name("courses/edit"))
				.andDo(result -> course.removeLecture(lecture))
				.andExpect(model().attribute("course", equalTo(course)));
		
		verify(lectureService, times(1)).removeById(1);
		verifyNoMoreInteractions(lectureService);
		
		verify(courseService, times(1)).getById(1);
		verifyNoMoreInteractions(courseService);
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
