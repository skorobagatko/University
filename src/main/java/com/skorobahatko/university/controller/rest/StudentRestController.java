package com.skorobahatko.university.controller.rest;

import com.skorobahatko.university.domain.Student;
import com.skorobahatko.university.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentRestController {
	
	private StudentService studentService;

	@Autowired
	public StudentRestController(StudentService studentService) {
		this.studentService = studentService;
	}

	@GetMapping()
	public ResponseEntity<List<Student>> getAllStudents() {
		List<Student> students = studentService.getAll();

		return new ResponseEntity<>(students, HttpStatus.OK);
	}
	
	@PostMapping()
	public ResponseEntity<Student> addStudent(@RequestBody Student student) {
		studentService.add(student);

		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Student> getStudentById(@PathVariable("id") int id) {
		Student student = studentService.getById(id);

		return new ResponseEntity<>(student, HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Student> updateStudentById(@RequestBody Student student) {
		studentService.update(student);

		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Student> deleteStudentById(@PathVariable("id") int id) {
		studentService.removeById(id);

		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("/{studentId}/course/{courseId}")
	public ResponseEntity<Student> addCourseToStudent(
			@PathVariable("studentId") int studentId,
			@PathVariable("courseId") int courseId) {
		studentService.addCourseToStudentById(studentId, courseId);
		Student student = studentService.getById(studentId);
			
		return new ResponseEntity<>(student, HttpStatus.OK);
	}
	
	@DeleteMapping("/{studentId}/course/{courseId}")
	public ResponseEntity<Student> deleteCourseFromStudent(
			@PathVariable("studentId") int studentId,
			@PathVariable("courseId") int courseId) {
		studentService.deleteStudentsCourseById(studentId, courseId);
		Student student = studentService.getById(studentId);
			
		return new ResponseEntity<>(student, HttpStatus.OK);
	}

}
