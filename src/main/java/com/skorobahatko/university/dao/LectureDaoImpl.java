package com.skorobahatko.university.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;

import com.skorobahatko.university.domain.Lecture;

public class LectureDaoImpl implements LectureDao {

	private static final String LECTURE_ID = "lecture_id";
	private static final String COURSE_ID = "course_id";
	private static final String LECTURE_NAME = "lecture_name";
	private static final String LECTURE_DATE = "lecture_date";
	private static final String LECTURE_START_TIME = "lecture_start_time";
	private static final String LECTURE_END_TIME = "lecture_end_time";
	private static final String LECTURE_ROOM_NUMBER = "lecture_room_number";
	
	private static final String GET_ALL_SQL = "SELECT * FROM lectures";

	private static final String GET_BY_ID_SQL = "SELECT * FROM lectures WHERE lecture_id = ?";

	private static final String GET_BY_COURSE_ID_SQL = "SELECT * FROM lectures WHERE course_id = ?";

	private static final String ADD_SQL = "INSERT INTO lectures "
			+ "(course_id, lecture_name, lecture_date, lecture_start_time, lecture_end_time, lecture_room_number) "
			+ "VALUES (?, ?, ?, ?, ?, ?) RETURNING lecture_id;";

	private static final String REMOVE_BY_ID_SQL = "DELETE FROM lectures WHERE lecture_id = ?;";

	private JdbcTemplate jdbcTemplate;

	public LectureDaoImpl() {
		// NOP
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public List<Lecture> getAll() {
		return jdbcTemplate.query(GET_ALL_SQL, 
				(rs, rowNum) -> {
					int lectureId = rs.getInt(LECTURE_ID);
					int courseId = rs.getInt(COURSE_ID);
					String lectureName = rs.getString(LECTURE_NAME);
					LocalDate lectureDate = rs.getDate(LECTURE_DATE).toLocalDate();
					LocalTime lectureStartTime = rs.getTime(LECTURE_START_TIME).toLocalTime();
					LocalTime lectureEndTime = rs.getTime(LECTURE_END_TIME).toLocalTime();
					int lectureRoomNumber = rs.getInt(LECTURE_ROOM_NUMBER);
					return new Lecture(lectureId, lectureName, courseId, lectureDate, lectureStartTime,
							lectureEndTime, lectureRoomNumber);
		});
	}

	@Override
	public Optional<Lecture> getById(int id) {
		return jdbcTemplate.query(con -> con.prepareStatement(GET_BY_ID_SQL), ps -> ps.setInt(1, id), rs -> {
			if (rs.next()) {
				int lectureId = rs.getInt(LECTURE_ID);
				int courseId = rs.getInt(COURSE_ID);
				String lectureName = rs.getString(LECTURE_NAME);
				LocalDate lectureDate = rs.getDate(LECTURE_DATE).toLocalDate();
				LocalTime lectureStartTime = rs.getTime(LECTURE_START_TIME).toLocalTime();
				LocalTime lectureEndTime = rs.getTime(LECTURE_END_TIME).toLocalTime();
				int lectureRoomNumber = rs.getInt(LECTURE_ROOM_NUMBER);
				Lecture lecture = new Lecture(lectureId, lectureName, courseId, lectureDate, lectureStartTime,
						lectureEndTime, lectureRoomNumber);
				return Optional.of(lecture);
			} else {
				return Optional.empty();
			}
		});
	}

	@Override
	public List<Lecture> getByCourseId(int courseId) {
		return jdbcTemplate.query(GET_BY_COURSE_ID_SQL, ps -> ps.setInt(1, courseId), (rs, rowNum) -> {
			int lectureId = rs.getInt(LECTURE_ID);
			String lectureName = rs.getString(LECTURE_NAME);
			LocalDate lectureDate = rs.getDate(LECTURE_DATE).toLocalDate();
			LocalTime lectureStartTime = rs.getTime(LECTURE_START_TIME).toLocalTime();
			LocalTime lectureEndTime = rs.getTime(LECTURE_END_TIME).toLocalTime();
			int lectureRoomNumber = rs.getInt(LECTURE_ROOM_NUMBER);
			return new Lecture(lectureId, lectureName, courseId, lectureDate, lectureStartTime, lectureEndTime,
					lectureRoomNumber);
		});
	}
	
	@Override
	public void addAll(List<Lecture> lectures) {
		lectures.forEach(this::add);
	}

	@Override
	public void add(Lecture lecture) {
		int lectureId = jdbcTemplate.queryForObject(ADD_SQL, Long.class, lecture.getCourseId(), lecture.getName(),
				lecture.getDate(), lecture.getStartTime(), lecture.getEndTime(), lecture.getRoomNumber()).intValue();
		lecture.setId(lectureId);
	}

	@Override
	public void removeById(int id) {
		jdbcTemplate.update(REMOVE_BY_ID_SQL, id);
	}

}
