package com.skorobahatko.university.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skorobahatko.university.domain.Participant;
import com.skorobahatko.university.domain.Timetable;
import com.skorobahatko.university.service.ParticipantService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/timetables")
public class TimetableRestController {
	
	private ParticipantService participantService;
	
	@Autowired
	public TimetableRestController(ParticipantService participantService) {
		this.participantService = participantService;
	}
	
	@GetMapping("/month/{participantId}")
	@ApiOperation(value = "Returns month timetable for participant with id provided", 
			notes = "Provide participant ID to get month timetable", 
			response = Timetable.class)
	public ResponseEntity<Timetable> getMonthTimetableByParticipantId(
			@ApiParam(value = "ID value for the participant you need to retrieve timetable", required = true)
			@PathVariable("participantId") int participantId) {
		Participant participant = participantService.getById(participantId);
		Timetable timetable = Timetable.getMonthTimetable(participant);
		
		return new ResponseEntity<>(timetable, HttpStatus.OK);
	}
	
	@GetMapping("/day/{participantId}")
	@ApiOperation(value = "Returns day timetable for participant with id provided", 
			notes = "Provide participant ID to get day timetable", 
			response = Timetable.class)
	public ResponseEntity<Timetable> getDayTimetableForParticipant(
			@ApiParam(value = "ID value for the participant you need to retrieve timetable", required = true)
			@PathVariable("participantId") int participantId) {
		Participant participant = participantService.getById(participantId);
		Timetable timetable = Timetable.getDayTimetable(participant);
		
		return new ResponseEntity<>(timetable, HttpStatus.OK);
	}

}
