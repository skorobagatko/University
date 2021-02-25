package com.skorobahatko.university.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.skorobahatko.university.domain.Course;
import com.skorobahatko.university.service.CourseService;

@Controller
@RequestMapping("/courses")
public class CourseController {
	
	private static final String REDIRECT_TO_COURSES_LIST_PAGE = "redirect:/courses";
	private static final String REDIRECT_TO_COURSE_PAGE = "redirect:/courses/{id}";
	private static final String COURSE_EDIT_PAGE = "courses/edit";
	
	@Autowired
	private CourseService courseService;
	
	@GetMapping()
	public String getAllCourses(Model model) {
		List<Course> courses = courseService.getAll();
		model.addAttribute("courses", courses);
		
		return "courses/all";
	}
	
	@PostMapping("/new")
	public String addCourse(
			@RequestParam String courseName, 
			RedirectAttributes redirectAttributes) {
		
		Course newCourse = courseService.add(new Course(courseName));
		
		redirectAttributes.addAttribute("id", newCourse.getId());
		
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
	public String updateCourseById(
			@Valid @ModelAttribute("course") Course course,
			BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
			return COURSE_EDIT_PAGE;
		}
		
		courseService.update(course);
		
		return REDIRECT_TO_COURSES_LIST_PAGE;
	}
	
	@DeleteMapping("/{id}")
	public String deleteCourseById(@PathVariable("id") int id) {
		courseService.removeById(id);
		
		return REDIRECT_TO_COURSES_LIST_PAGE;
	}

}
