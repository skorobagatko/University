package com.skorobahatko.university.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.skorobahatko.university.domain.Teacher;
import com.skorobahatko.university.service.CourseService;
import com.skorobahatko.university.service.ParticipantService;

@Controller
@RequestMapping("/teachers")
public class TeacherController {
	
	private static final String REDIRECT_TO_TEACHERS_LIST_PAGE = "redirect:/teachers";
	private static final String TEACHER_EDIT_PAGE = "teachers/edit";
	
	private static final String TEACHER = "teacher";
	
	@Autowired
	private ParticipantService participantService;
	
	@Autowired
	private CourseService courseService;
	
	@GetMapping()
	public String getAllTeachers(Model model) {
		List<Teacher> teachers = participantService.getAllTeachers();
		List<Course> courses = courseService.getAll();
		
		model.addAttribute("teachers", teachers);
		model.addAttribute("courses", courses);
		
		return "teachers/all";
	}
	
	@PostMapping("/new")
	public String addTeacher(
			@RequestParam String firstName,
			@RequestParam String lastName,
			@RequestParam int courseId) {
		
		Teacher newTeacher = new Teacher(firstName, lastName);
		participantService.add(newTeacher);
		
		participantService.addParticipantCourseById(newTeacher.getId(), courseId);
		
		return REDIRECT_TO_TEACHERS_LIST_PAGE;
	}

	@GetMapping("/{id}")
	public String getTeacherById(@PathVariable("id") int id, Model model) {
		Teacher teacher = (Teacher) participantService.getById(id);
		model.addAttribute(TEACHER, teacher);
	
		return "teachers/teacher";
	}
	
	@GetMapping("/{id}/edit")
	public String editTeacherById(@PathVariable("id") int id, Model model) {
		Teacher teacher = (Teacher) participantService.getById(id);
		model.addAttribute(TEACHER, teacher);
		
		List<Course> notAttendedCourses = getNotAttendedCoursesFor(teacher);
		model.addAttribute("notAttendedCourses", notAttendedCourses);
		
		return TEACHER_EDIT_PAGE;
	}
	
	@PostMapping("/{id}/course/add")
	public String addCourseToTeacher(
			@PathVariable("id") int teacherId,
			@RequestParam int courseId,
			Model model) {
		
		participantService.addParticipantCourseById(teacherId, courseId);
		
		Teacher teacher = (Teacher) participantService.getById(teacherId);
		model.addAttribute(TEACHER, teacher);
		
		return TEACHER_EDIT_PAGE;
	}
	
	@DeleteMapping("/{teacherId}/course/{courseId}")
	public String deleteCourseFromTeacher(
			@PathVariable("teacherId") int teacherId,
			@PathVariable("courseId") int courseId,
			Model model) {
		
		participantService.removeParticipantCourseById(teacherId, courseId);
		
		Teacher teacher = (Teacher) participantService.getById(teacherId);
		model.addAttribute(TEACHER, teacher);
		
		return TEACHER_EDIT_PAGE;
	}
	
	@PatchMapping("/{id}")
	public String updateTeacherById(@ModelAttribute("teacher") Teacher teacher) {
		participantService.update(teacher);
		
		return REDIRECT_TO_TEACHERS_LIST_PAGE;
	}
	
	@DeleteMapping("/{id}")
	public String deleteTeacherById(@PathVariable("id") int id) {
		participantService.removeById(id);
		
		return REDIRECT_TO_TEACHERS_LIST_PAGE;
	}
	
	private List<Course> getNotAttendedCoursesFor(Teacher teacher) {
		List<Course> teacherCourses = teacher.getCourses();
		List<Course> result = courseService.getAll();
		result.removeAll(teacherCourses);
		
		return result;
	}

}
