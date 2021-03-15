package com.skorobahatko.university.controller;

import static com.skorobahatko.university.util.TestUtils.getTestLectureWithCourseId;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.skorobahatko.university.domain.Course;
import com.skorobahatko.university.domain.Lecture;
import com.skorobahatko.university.service.CourseService;
import com.skorobahatko.university.service.LectureService;

@RunWith(MockitoJUnitRunner.class)
class LectureControllerTest {
	
	private LectureController lectureController;
	private LectureService lectureService;
	private CourseService courseService;
	
	@BeforeEach
	public void init() {
		lectureService = Mockito.mock(LectureService.class);
		courseService = Mockito.mock(CourseService.class);
		lectureController = new LectureController(courseService, lectureService);
	}
	
	@Test
	void redirectIsSuccessfulForNewAddedLecture() {
		int courseId = 1;
		String lectureName = "Test Lecture";
		LocalDate lectureDate = LocalDate.now();
		LocalTime lectureStartTime = LocalTime.of(8, 0);
		LocalTime lectureEndTime = LocalTime.of(9, 0);
		int roomNumber = 100;
		RedirectAttributes attributes = Mockito.mock(RedirectAttributes.class);
		
		Course course = new Course(1, "Test Course");
		when(courseService.getById(courseId)).thenReturn(course);
		
		assertThat(lectureController.addLecture(
				courseId, 
				lectureName, 
				lectureDate, 
				lectureStartTime, 
				lectureEndTime,
				roomNumber,
				attributes), equalTo("redirect:/courses/{id}/edit"));
	}
	
	@Test
	void editLectureViewIsOpenedForLectureWithRequestedId() {
		Model model = new ExtendedModelMap();
		
		Lecture lecture = getTestLectureWithCourseId(1);
		when(lectureService.getById(1)).thenReturn(lecture);
		
		assertThat(lectureController.editLectureById(1, model), equalTo("lectures/edit"));
		assertThat(model.asMap(), hasEntry("lecture", lecture));
	}
	
	@Test
	void redirectIsSuccessfulForUpdatedLecture() {
		Lecture lecture = getTestLectureWithCourseId(1);
		BindingResult bindingResult = Mockito.mock(BindingResult.class);
		RedirectAttributes attributes = Mockito.mock(RedirectAttributes.class);
		
		assertThat(lectureController.updateLecture(
				lecture, 
				bindingResult, 
				attributes), equalTo("redirect:/courses/{id}/edit"));
	}
	
	@Test
	void lectureEditViewIsOpenedIfUserInputErrorsWasFoundWhileUpdatingLecture() {
		Lecture lecture = getTestLectureWithCourseId(1);
		BindingResult bindingResult = Mockito.mock(BindingResult.class);
		RedirectAttributes attributes = Mockito.mock(RedirectAttributes.class);
		
		when(bindingResult.hasErrors()).thenReturn(true);
		
		assertThat(lectureController.updateLecture(
				lecture, 
				bindingResult, 
				attributes), equalTo("lectures/edit"));
	}
	
	@Test
	void redirectIsSuccessfulAfterDeletingLecture() {
		int lectureId = 1;
		int courseId = 1;
		RedirectAttributes attributes = Mockito.mock(RedirectAttributes.class);
		
		assertThat(lectureController.deleteLectureById(
				lectureId, 
				courseId, 
				attributes), equalTo("redirect:/courses/{id}/edit"));
	}

}
