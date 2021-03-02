package com.skorobahatko.university.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skorobahatko.university.domain.Course;
import com.skorobahatko.university.service.CourseService;

@RestController
@RequestMapping("/api/courses")
public class CourseRestController {

	private CourseService courseService;

	@Autowired
	public CourseRestController(CourseService courseService) {
		this.courseService = courseService;
	}

	@GetMapping()
	public ResponseEntity<List<Course>> getAllCourses() {
		List<Course> courses = courseService.getAll();

		return new ResponseEntity<>(courses, HttpStatus.OK);
	}

	@PostMapping()
	public ResponseEntity<Course> addCourse(@RequestBody Course course) {
		courseService.add(course);

		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Course> getCourseById(@PathVariable("id") int id) {
		Course course = courseService.getById(id);

		return new ResponseEntity<>(course, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Course> updateCourseById(@RequestBody Course course) {
		courseService.update(course);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Course> deleteCourseById(@PathVariable("id") int id) {
		courseService.removeById(id);

		return new ResponseEntity<>(HttpStatus.OK);
	}

}
