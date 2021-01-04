package com.skorobahatko.university.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.skorobahatko.university.domain.Course;
import com.skorobahatko.university.domain.Lecture;
import com.skorobahatko.university.service.CourseService;
import com.skorobahatko.university.service.LectureService;

@Controller
@RequestMapping("/courses")
public class CourseController {
	
	private static final String REDIRECT_TO_COURSES_LIST_PAGE = "redirect:/courses";
	private static final String REDIRECT_TO_COURSE_PAGE = "redirect:/courses/{id}";
	private static final String COURSE_EDIT_PAGE = "courses/edit";
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private LectureService lectureService;
	
	@GetMapping()
	public String getAllCourses(Model model) {
		List<Course> courses = courseService.getAll();
		model.addAttribute("courses", courses);
		
		return "courses/all";
	}
	
	@PostMapping("/new")
	public String addCourse(@RequestParam String courseName, Model model) {
		Course newCourse = new Course(courseName);
		courseService.add(newCourse);
		
		model.addAttribute("id", newCourse.getId());
		
		return REDIRECT_TO_COURSE_PAGE;
	}

	@GetMapping("/{id}")
	public String getCourseById(@PathVariable("id") int id, Model model) {
		Course course = courseService.getById(id);
		model.addAttribute("course", course);
	
		return "courses/course";
	}
	
	@GetMapping("/{id}/edit")
	public String editCourseById(@PathVariable("id") int id, Model model) {
		Course course = courseService.getById(id);
		model.addAttribute("course", course);
		
		return COURSE_EDIT_PAGE;
	}
	
	@PatchMapping("/{id}")
	public String updateCourseById(@ModelAttribute("course") Course course) {
		courseService.update(course);
		
		return REDIRECT_TO_COURSES_LIST_PAGE;
	}
	
	@PutMapping("/{courseId}/lecture/new")
	public String addNewLecture(
			@PathVariable("courseId") int courseId, 
			@RequestParam String lectureName,
			@RequestParam("lectureDate") 
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate lectureDate,
			@RequestParam("lectureStartTime") 
			@DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime lectureStartTime,
			@RequestParam("lectureEndTime")
			@DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime lectureEndTime,
			@RequestParam int roomNumber,
			Model model) {
		
		Lecture lecture = new Lecture(lectureName, courseId, lectureDate, lectureStartTime, lectureEndTime, roomNumber);
		lectureService.add(lecture);
		
		Course course = courseService.getById(courseId);
		model.addAttribute("course", course);
		
		return COURSE_EDIT_PAGE;
	}
	
	@DeleteMapping("/{courseId}/lecture/{lectureId}")
	public String deleteLectureById(@PathVariable("courseId") int courseId, @PathVariable("lectureId") int lectureId, Model model) {
		lectureService.removeById(lectureId);
		
		Course course = courseService.getById(courseId);
		model.addAttribute("course", course);
		
		return COURSE_EDIT_PAGE;
	}
	
	@DeleteMapping("/{id}")
	public String deleteCourseById(@PathVariable("id") int id) {
		courseService.removeById(id);
		
		return REDIRECT_TO_COURSES_LIST_PAGE;
	}

}
