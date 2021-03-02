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

@RestController
@RequestMapping("/api/lectures")
public class LectureRestController {

	private LectureService lectureService;

	@Autowired
	public LectureRestController(LectureService lectureService) {
		this.lectureService = lectureService;
	}

	@GetMapping()
	public ResponseEntity<List<Lecture>> getAllLectures() {
		List<Lecture> lectures = lectureService.getAll();

		return new ResponseEntity<>(lectures, HttpStatus.OK);
	}

	@PostMapping()
	public ResponseEntity<Lecture> addLecture(@RequestBody Lecture lecture) {
		lecture = lectureService.add(lecture);

		return new ResponseEntity<>(lecture, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Lecture> getLectureById(@PathVariable("id") int id) {
		Lecture lecture = lectureService.getById(id);

		return new ResponseEntity<>(lecture, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Lecture> updateLecture(@RequestBody Lecture lecture) {
		lectureService.update(lecture);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Lecture> deleteLectureById(@PathVariable("id") int id) {
		lectureService.removeById(id);

		return new ResponseEntity<>(HttpStatus.OK);
	}

}
