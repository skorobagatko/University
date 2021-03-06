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

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/courses")
public class CourseRestController {

	private CourseService courseService;

	@Autowired
	public CourseRestController(CourseService courseService) {
		this.courseService = courseService;
	}

	@GetMapping()
	@ApiOperation(value = "Returns all courses", response = List.class)
	public ResponseEntity<List<Course>> getAllCourses() {
		List<Course> courses = courseService.getAll();

		return new ResponseEntity<>(courses, HttpStatus.OK);
	}

	@PostMapping()
	@ApiOperation(value = "Adds new course to the course list")
	public ResponseEntity<Course> addCourse(
			@ApiParam(value = "The new course that you need to add", required = true)
			@RequestBody Course course) {
		courseService.add(course);

		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "Returns course by its id",
			notes = "Provide an ID to look up specific course",
			response = Course.class)
	public ResponseEntity<Course> getCourseById(
			@ApiParam(value = "ID value for the course you need to retrieve", required = true)
			@PathVariable("id") int id) {
		Course course = courseService.getById(id);

		return new ResponseEntity<>(course, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	@ApiOperation(value = "Updates course", notes = "Provide the course for updating")
	public ResponseEntity<Course> updateCourse(
			@ApiParam(value = "The course that you need to update", required = true)
			@RequestBody Course course) {
		courseService.update(course);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@ApiOperation(value = "Deletes course by its id", notes = "Provide an ID to delete specific course")
	public ResponseEntity<Course> deleteCourseById(
			@ApiParam(value = "ID value for the course you need to delete", required = true)
			@PathVariable("id") int id) {
		courseService.removeById(id);

		return new ResponseEntity<>(HttpStatus.OK);
	}

}
