package com.skorobahatko.university.controller;

import java.time.LocalDate;
import java.time.LocalTime;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.skorobahatko.university.domain.Course;
import com.skorobahatko.university.domain.Lecture;
import com.skorobahatko.university.service.CourseService;
import com.skorobahatko.university.service.LectureService;

@Controller
@RequestMapping("/lectures")
public class LectureController {

	private static final String REDIRECT_TO_COURSE_EDIT_PAGE = "redirect:/courses/{id}/edit";
	
	private static final String COURSE = "course";

	@Autowired
	private CourseService courseService;

	@Autowired
	private LectureService lectureService;

	@PostMapping("/new")
	public String addLecture(
			@RequestParam int courseId, 
			@RequestParam String lectureName,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate lectureDate,
			@RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime lectureStartTime,
			@RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime lectureEndTime,
			@RequestParam int roomNumber, 
			Model model) {
		
		Lecture lecture = new Lecture(lectureName, courseId, lectureDate, lectureStartTime, lectureEndTime, roomNumber);
		lectureService.add(lecture);

		Course course = courseService.getById(courseId);
		model.addAttribute(COURSE, course);
		model.addAttribute("id", courseId);

		return REDIRECT_TO_COURSE_EDIT_PAGE;

	}

	@GetMapping("/{id}/edit")
	public String editLecture(@PathVariable("id") int id, Model model) {
		Lecture lecture = lectureService.getById(id);
		model.addAttribute("lecture", lecture);
		
		return "lectures/edit";
	}
	
	@PatchMapping("/{id}")
	public String updateLecture(
			@ModelAttribute("lecture") Lecture lecture,
			Model model) {
		
		lectureService.update(lecture);
		
		int courseId = lecture.getCourseId();
		Course course = courseService.getById(courseId);
		model.addAttribute(COURSE, course);
		model.addAttribute("id", courseId);
		
		return REDIRECT_TO_COURSE_EDIT_PAGE;
	}

	@DeleteMapping("/{lectureId}")
	public String deleteLectureById(
			@PathVariable("lectureId") int lectureId, 
			@RequestParam int courseId, 
			Model model) {

		lectureService.removeById(lectureId);

		Course course = courseService.getById(courseId);
		model.addAttribute(COURSE, course);
		model.addAttribute("id", courseId);

		return REDIRECT_TO_COURSE_EDIT_PAGE;
	}

}
