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
import com.skorobahatko.university.domain.Student;
import com.skorobahatko.university.service.CourseService;
import com.skorobahatko.university.service.ParticipantService;

@Controller
@RequestMapping("/students")
public class StudentController {
	
	private static final String REDIRECT_TO_STUDENTS_LIST_PAGE = "redirect:/students";
	private static final String STUDENT_EDIT_PAGE = "students/edit";
	
	private static final String STUDENT = "student";
	
	@Autowired
	private ParticipantService participantService;
	
	@Autowired
	private CourseService courseService;

	@GetMapping()
	public String getAllStudents(Model model) {
		List<Student> students = participantService.getAllStudents();
		model.addAttribute("students", students);
		
		return "students/all";
	}
	
	@PostMapping("/new")
	public String addStudent(
			@RequestParam String firstName,
			@RequestParam String lastName,
			@RequestParam int courseId) {
		
		Student newStudent = new Student(firstName, lastName);
		participantService.add(newStudent);
		
		participantService.addParticipantCourseById(newStudent.getId(), courseId);
		
		return REDIRECT_TO_STUDENTS_LIST_PAGE;
	}
	
	@GetMapping("/{id}")
	public String getStudentById(@PathVariable("id") int id, Model model) {
		Student student = (Student) participantService.getById(id);
		model.addAttribute(STUDENT, student);
	
		return "students/student";
	}
	
	@GetMapping("/{id}/edit")
	public String editStudentById(@PathVariable("id") int id, Model model) {
		Student student = (Student) participantService.getById(id);
		model.addAttribute(STUDENT, student);
		
		List<Course> notAttendedCourses = getNotAttendedCoursesFor(student);
		model.addAttribute("notAttendedCourses", notAttendedCourses);
		
		return STUDENT_EDIT_PAGE;
	}
	
	@PostMapping("/{id}/course/add")
	public String addCourseToStudent(
			@PathVariable("id") int studentId,
			@RequestParam int courseId,
			Model model) {
		
		participantService.addParticipantCourseById(studentId, courseId);
		
		Student student = (Student) participantService.getById(studentId);
		model.addAttribute(STUDENT, student);
		
		return STUDENT_EDIT_PAGE;
	}
	
	@DeleteMapping("/{studentId}/course/{courseId}")
	public String deleteCourseFromStudent(
			@PathVariable("studentId") int studentId,
			@PathVariable("courseId") int courseId,
			Model model) {
		
		participantService.removeParticipantCourseById(studentId, courseId);
		
		Student student = (Student) participantService.getById(studentId);
		model.addAttribute(STUDENT, student);
		
		return STUDENT_EDIT_PAGE;
	}
	
	@PatchMapping("/{id}")
	public String updateStudentById(@ModelAttribute("student") Student student) {
		participantService.update(student);
		
		return REDIRECT_TO_STUDENTS_LIST_PAGE;
	}
	
	@DeleteMapping("/{id}")
	public String deleteStudentById(@PathVariable("id") int id) {
		participantService.removeById(id);
		
		return REDIRECT_TO_STUDENTS_LIST_PAGE;
	}
	
	private List<Course> getNotAttendedCoursesFor(Student student) {
		List<Course> studentCourses = student.getCourses();
		List<Course> result = courseService.getAll();
		result.removeAll(studentCourses);
		
		return result;
	}

}
