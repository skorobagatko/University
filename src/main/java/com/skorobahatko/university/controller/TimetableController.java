package com.skorobahatko.university.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.skorobahatko.university.domain.Participant;
import com.skorobahatko.university.domain.Timetable;
import com.skorobahatko.university.service.ParticipantService;

@Controller
@RequestMapping("/timetables")
public class TimetableController {
	
	@Autowired
	private ParticipantService participantService;
	
	@GetMapping()
	public String index(Model model) {
		List<Participant> participants = participantService.getAll();
		model.addAttribute("participants", participants);
		
		return "timetables/index";
	}
	
	@GetMapping("/participant")
	public String getTimetableForParticipant(
			@RequestParam int participantId,
			Model model) {
		
		Timetable timetable = getTimetableByParticipantId(participantId);
		
		model.addAttribute("timetable", timetable);
		
		return "timetables/timetable";
	}
	
	private Timetable getTimetableByParticipantId(int participantId) {
		Participant participant = participantService.getById(participantId);
		return Timetable.getMonthTimetable(participant);
	}

}
