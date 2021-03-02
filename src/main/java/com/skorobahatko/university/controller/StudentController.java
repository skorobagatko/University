package com.skorobahatko.university.controller;

import com.skorobahatko.university.domain.Course;
import com.skorobahatko.university.domain.Student;
import com.skorobahatko.university.service.CourseService;
import com.skorobahatko.university.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {
	
	private static final String REDIRECT_TO_STUDENTS_LIST_PAGE = "redirect:/students";
	private static final String STUDENT_EDIT_PAGE = "students/edit";
	
	private static final String STUDENT = "student";
	
	private StudentService studentService;
	private CourseService courseService;

	@Autowired
	public StudentController(StudentService studentService, CourseService courseService) {
		this.studentService = studentService;
		this.courseService = courseService;
	}

	@GetMapping()
	public String getAllStudents(Model model) {
		List<Student> students = studentService.getAll();
		List<Course> courses = courseService.getAll();
		
		model.addAttribute("students", students);
		model.addAttribute("courses", courses);
		
		return "students/all";
	}
	
	@PostMapping("/new")
	public String addStudent(
			@RequestParam String firstName,
			@RequestParam String lastName,
			@RequestParam int courseId) {
		
		Student newStudent = new Student(firstName, lastName);
		
		newStudent = studentService.add(newStudent);
		studentService.addCourseToStudentById(newStudent.getId(), courseId);
		
		return REDIRECT_TO_STUDENTS_LIST_PAGE;
	}
	
	@GetMapping("/{id}")
	public String getStudentById(@PathVariable("id") int id, Model model) {
		Student student = studentService.getById(id);

		model.addAttribute(STUDENT, student);
	
		return "students/student";
	}
	
	@GetMapping("/{id}/edit")
	public String editStudentById(@PathVariable("id") int id, Model model) {
		Student student = studentService.getById(id);

		model.addAttribute(STUDENT, student);
		
		List<Course> notAttendedCourses = getNotAttendedCoursesFor(student);
		model.addAttribute("notAttendedCourses", notAttendedCourses);
		
		return STUDENT_EDIT_PAGE;
	}
	
	@PatchMapping("/{id}")
	public String updateStudentById(
			@Valid @ModelAttribute("student") Student student,
			BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
			return STUDENT_EDIT_PAGE;
		}
		
		studentService.update(student);

		return REDIRECT_TO_STUDENTS_LIST_PAGE;
	}
	
	@PostMapping("/{id}/course/add")
	public String addCourseToStudent(
			@PathVariable("id") int studentId,
			@RequestParam int courseId,
			Model model) {
		
		studentService.addCourseToStudentById(studentId, courseId);
		Student student = studentService.getById(studentId);
		
		model.addAttribute(STUDENT, student);
		
		return STUDENT_EDIT_PAGE;
	}
	
	@DeleteMapping("/{studentId}/course/{courseId}")
	public String deleteCourseFromStudent(
			@PathVariable("studentId") int studentId,
			@PathVariable("courseId") int courseId,
			Model model) {
		
		studentService.deleteStudentsCourseById(studentId, courseId);
		Student student = studentService.getById(studentId);
		
		model.addAttribute(STUDENT, student);
		
		return STUDENT_EDIT_PAGE;
	}
	
	@DeleteMapping("/{id}")
	public String deleteStudentById(@PathVariable("id") int id) {
		studentService.removeById(id);

		return REDIRECT_TO_STUDENTS_LIST_PAGE;
	}
	
	private List<Course> getNotAttendedCoursesFor(Student student) {
		List<Course> studentCourses = student.getCourses();
		List<Course> result = courseService.getAll();
		result.removeAll(studentCourses);
		
		return result;
	}

}
