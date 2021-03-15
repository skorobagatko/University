package com.skorobahatko.university.controller.rest;

import com.skorobahatko.university.domain.Student;
import com.skorobahatko.university.service.StudentService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

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
	@ApiOperation(value = "Returns all students", response = List.class)
	public ResponseEntity<List<Student>> getAllStudents() {
		List<Student> students = studentService.getAll();

		return new ResponseEntity<>(students, HttpStatus.OK);
	}

	@PostMapping()
	@ApiOperation(value = "Adds new student to the student list")
	public ResponseEntity<Student> addStudent(
			@ApiParam(value = "The new student that you need to add", required = true) 
			@RequestBody Student student) {
		studentService.add(student);

		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "Returns student by its id", 
			notes = "Provide an ID to look up specific student", 
			response = Student.class)
	public ResponseEntity<Student> getStudentById(
			@ApiParam(value = "ID value for the student you need to retrieve", required = true) 
			@PathVariable("id") int id) {
		Student student = studentService.getById(id);

		return new ResponseEntity<>(student, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	@ApiOperation(value = "Updates student", notes = "Provide the student for updating")
	public ResponseEntity<Student> updateStudent(
			@ApiParam(value = "The student that you need to update", required = true) 
			@RequestBody Student student) {
		studentService.update(student);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@ApiOperation(value = "Deletes student by its id", notes = "Provide an ID to delete specific student")
	public ResponseEntity<Student> deleteStudentById(
			@ApiParam(value = "ID value for the student you need to delete", required = true) 
			@PathVariable("id") int id) {
		studentService.removeById(id);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/{studentId}/course/{courseId}")
	@ApiOperation(value = "Adds course to the student's course list by their IDs", 
			notes = "Provide an ID for both course and student", 
			response = Student.class)
	public ResponseEntity<Student> addStudentCourse(
			@ApiParam(value = "The student's ID for whom you want to add a course", required = true)
			@PathVariable("studentId") int studentId,
			@ApiParam(value = "The course's ID to be added to the student's course list", required = true)
			@PathVariable("courseId") int courseId) {
		studentService.addCourseToStudentById(studentId, courseId);
		Student student = studentService.getById(studentId);

		return new ResponseEntity<>(student, HttpStatus.OK);
	}

	@DeleteMapping("/{studentId}/course/{courseId}")
	@ApiOperation(value = "Deletes course from the student's course list by their IDs", 
			notes = "Provide an ID for both course and student", 
			response = Student.class)
	public ResponseEntity<Student> deleteStudentCourse(
			@ApiParam(value = "The student's ID for whom you want to delete a course", required = true)
			@PathVariable("studentId") int studentId,
			@ApiParam(value = "The course's ID to be deleted from the student's course list", required = true)
			@PathVariable("courseId") int courseId) {
		studentService.deleteStudentsCourseById(studentId, courseId);
		Student student = studentService.getById(studentId);

		return new ResponseEntity<>(student, HttpStatus.OK);
	}

}
