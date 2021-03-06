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

import com.skorobahatko.university.domain.Lecture;
import com.skorobahatko.university.service.LectureService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/lectures")
public class LectureRestController {

	private LectureService lectureService;

	@Autowired
	public LectureRestController(LectureService lectureService) {
		this.lectureService = lectureService;
	}

	@GetMapping()
	@ApiOperation(value = "Returns all lectures", response = List.class)
	public ResponseEntity<List<Lecture>> getAllLectures() {
		List<Lecture> lectures = lectureService.getAll();

		return new ResponseEntity<>(lectures, HttpStatus.OK);
	}

	@PostMapping()
	@ApiOperation(value = "Adds new lecture to the lecture list")
	public ResponseEntity<Lecture> addLecture(
			@ApiParam(value = "The new lecture that you need to add", required = true)
			@RequestBody Lecture lecture) {
		lecture = lectureService.add(lecture);

		return new ResponseEntity<>(lecture, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "Returns lecture by its id",
			notes = "Provide an ID to look up specific lecture",
			response = Lecture.class)
	public ResponseEntity<Lecture> getLectureById(
			@ApiParam(value = "ID value for the lecture you need to retrieve", required = true)
			@PathVariable("id") int id) {
		Lecture lecture = lectureService.getById(id);

		return new ResponseEntity<>(lecture, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	@ApiOperation(value = "Updates lecture", notes = "Provide the lecture for updating")
	public ResponseEntity<Lecture> updateLecture(
			@ApiParam(value = "The lecture that you need to update", required = true)
			@RequestBody Lecture lecture) {
		lectureService.update(lecture);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@ApiOperation(value = "Deletes lecture by its id", notes = "Provide an ID to delete specific lecture")
	public ResponseEntity<Lecture> deleteLectureById(
			@ApiParam(value = "ID value for the lecture you need to delete", required = true)
			@PathVariable("id") int id) {
		lectureService.removeById(id);

		return new ResponseEntity<>(HttpStatus.OK);
	}

}
