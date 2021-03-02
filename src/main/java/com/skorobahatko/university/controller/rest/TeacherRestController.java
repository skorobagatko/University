package com.skorobahatko.university.controller.rest;

import com.skorobahatko.university.domain.Teacher;
import com.skorobahatko.university.service.TeacherService;
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
	public ResponseEntity<List<Teacher>> getAllTeachers() {
		List<Teacher> teachers = teacherService.getAll();

		return new ResponseEntity<>(teachers, HttpStatus.OK);
	}

	@PostMapping()
	public ResponseEntity<Teacher> addTeacher(@RequestBody Teacher teacher) {
		teacherService.add(teacher);

		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Teacher> getTeacherById(@PathVariable("id") int id) {
		Teacher teacher = teacherService.getById(id);

		return new ResponseEntity<>(teacher, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Teacher> updateTeacherById(@RequestBody Teacher teacher) {
		teacherService.update(teacher);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Teacher> deleteTeacherById(@PathVariable("id") int id) {
		teacherService.removeById(id);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/{teacherId}/course/{courseId}")
	public ResponseEntity<Teacher> addCourseToTeacher(@PathVariable("teacherId") int teacherId,
			@PathVariable("courseId") int courseId) {
		teacherService.addCourseToTeacherById(teacherId, courseId);
		Teacher teacher = teacherService.getById(teacherId);

		return new ResponseEntity<>(teacher, HttpStatus.OK);
	}

	@DeleteMapping("/{teacherId}/course/{courseId}")
	public ResponseEntity<Teacher> deleteCourseFromTeacher(@PathVariable("teacherId") int teacherId,
			@PathVariable("courseId") int courseId) {
		teacherService.deleteTeachersCourseById(teacherId, courseId);
		Teacher teacher = teacherService.getById(teacherId);

		return new ResponseEntity<>(teacher, HttpStatus.OK);
	}

}
