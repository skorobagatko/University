package com.skorobahatko.university.controller.rest;

import com.skorobahatko.university.domain.Student;
import com.skorobahatko.university.domain.Teacher;
import com.skorobahatko.university.service.TeacherService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherRestController {

	private TeacherService teacherService;

	@Autowired
	public TeacherRestController(TeacherService teacherService) {
		this.teacherService = teacherService;
	}

	@GetMapping()
	@ApiOperation(value = "Returns all teachers", response = List.class)
	public ResponseEntity<List<Teacher>> getAllTeachers() {
		List<Teacher> teachers = teacherService.getAll();

		return new ResponseEntity<>(teachers, HttpStatus.OK);
	}

	@PostMapping()
	@ApiOperation(value = "Adds new teacher to the teacher list")
	public ResponseEntity<Teacher> addTeacher(
			@ApiParam(value = "The new teacher that you need to add", required = true)
			@RequestBody Teacher teacher) {
		teacherService.add(teacher);

		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "Returns teacher by its id", 
			notes = "Provide an ID to look up specific teacher", 
			response = Teacher.class)
	public ResponseEntity<Teacher> getTeacherById(
			@ApiParam(value = "ID value for the teacher you need to retrieve", required = true) 
			@PathVariable("id") int id) {
		Teacher teacher = teacherService.getById(id);

		return new ResponseEntity<>(teacher, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	@ApiOperation(value = "Updates teacher", notes = "Provide the teacher for updating")
	public ResponseEntity<Teacher> updateTeacherById(
			@ApiParam(value = "The teacher that you need to update", required = true) 
			@RequestBody Teacher teacher) {
		teacherService.update(teacher);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@ApiOperation(value = "Deletes teacher by its id", notes = "Provide an ID to delete specific teacher")
	public ResponseEntity<Teacher> deleteTeacherById(
			@ApiParam(value = "ID value for the teacher you need to delete", required = true) 
			@PathVariable("id") int id) {
		teacherService.removeById(id);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/{teacherId}/course/{courseId}")
	@ApiOperation(value = "Adds course to the teacher's course list by their IDs", 
			notes = "Provide an ID for both course and teacher", 
			response = Teacher.class)
	public ResponseEntity<Teacher> addCourseToTeacher(
			@ApiParam(value = "The teacher's ID for whom you want to add a course", required = true)
			@PathVariable("teacherId") int teacherId,
			@ApiParam(value = "The course's ID to be added to the teacher's course list", required = true)
			@PathVariable("courseId") int courseId) {
		teacherService.addCourseToTeacherById(teacherId, courseId);
		Teacher teacher = teacherService.getById(teacherId);

		return new ResponseEntity<>(teacher, HttpStatus.OK);
	}

	@DeleteMapping("/{teacherId}/course/{courseId}")
	@ApiOperation(value = "Deletes course from the teacher's course list by their IDs", 
			notes = "Provide an ID for both course and teacher", 
			response = Student.class)
	public ResponseEntity<Teacher> deleteCourseFromTeacher(
			@ApiParam(value = "The teacher's ID for whom you want to delete a course", required = true)
			@PathVariable("teacherId") int teacherId,
			@ApiParam(value = "The course's ID to be deleted from the teacher's course list", required = true)
			@PathVariable("courseId") int courseId) {
		teacherService.deleteTeachersCourseById(teacherId, courseId);
		Teacher teacher = teacherService.getById(teacherId);

		return new ResponseEntity<>(teacher, HttpStatus.OK);
	}

}
