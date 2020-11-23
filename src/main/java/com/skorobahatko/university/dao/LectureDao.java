package com.skorobahatko.university.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.skorobahatko.university.domain.Lecture;

public class LectureDao {
	
	private static final String LECTURE_ID = "lecture_id";
	private static final String LECTURE_NAME = "lecture_name";
	private static final String LECTURE_DATE = "lecture_date";
	private static final String LECTURE_START_TIME = "lecture_start_time";
	private static final String LECTURE_END_TIME = "lecture_end_time";
	private static final String LECTURE_ROOM_NUMBER = "lecture_room_number";
	
	
	private static final String GET_LECTURES_BY_COURSE_ID = "SELECT * FROM lectures WHERE course_id = ?";
	
	private static final String ADD_SQL = "INSERT INTO lectures "
			+ "(course_id, lecture_name, lecture_date, lecture_start_time, lecture_end_time, lecture_room_number) "
			+ "VALUES (?, ?, ?, ?, ?, ?);";
	
	private static final String REMOVE_LECTURE_BY_ID = "DELETE FROM lectures WHERE lecture_id = ?;";
	
	private JdbcTemplate jdbcTemplate;
	
	public LectureDao(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public List<Lecture> getByCourseId(int courseId) {
		return jdbcTemplate.query(
				GET_LECTURES_BY_COURSE_ID,
				ps -> ps.setInt(1, courseId),
				(rs, rowNum) -> {
					int lectureId = rs.getInt(LECTURE_ID);
					String lectureName = rs.getString(LECTURE_NAME);
					LocalDate lectureDate = rs.getDate(LECTURE_DATE).toLocalDate();
					LocalTime lectureStartTime = rs.getTime(LECTURE_START_TIME).toLocalTime();
					LocalTime lectureEndTime = rs.getTime(LECTURE_END_TIME).toLocalTime();
					int lectureRoomNumber = rs.getInt(LECTURE_ROOM_NUMBER);
					return new Lecture(lectureId, lectureName, courseId, lectureDate, lectureStartTime, lectureEndTime, lectureRoomNumber);
				}
		);
	}
	
	public void addAll(List<Lecture> lectures) {
		for (Lecture lecture : lectures) {
			add(lecture);
		}
	}
	
	public void add(Lecture lecture) {
		int lectureId = insertLecture(lecture);
		lecture.setId(lectureId);
	}
	
	private int insertLecture(Lecture lecture) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
	        PreparedStatement ps = connection.prepareStatement(ADD_SQL, Statement.RETURN_GENERATED_KEYS);
	  	    ps.setInt(1, lecture.getCourseId());
	  	    ps.setString(2, lecture.getName());
	  	    ps.setDate(3, Date.valueOf(lecture.getDate()));
	  	    ps.setTime(4, Time.valueOf(lecture.getStartTime()));
	  	    ps.setTime(5, Time.valueOf(lecture.getEndTime()));
			ps.setInt(6, lecture.getRoomNumber());
	  	    return ps;
		}, keyHolder);
		
		return (int) keyHolder.getKeys().get(LECTURE_ID);
	}
	
	public void removeById(int id) {
		jdbcTemplate.update(REMOVE_LECTURE_BY_ID, id);
	}

	

}
