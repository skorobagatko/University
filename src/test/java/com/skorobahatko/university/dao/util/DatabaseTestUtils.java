package com.skorobahatko.university.dao.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;
import java.util.List;

import javax.sql.DataSource;

import org.postgresql.ds.PGSimpleDataSource;

import com.skorobahatko.university.domain.Course;
import com.skorobahatko.university.domain.Lecture;
import com.skorobahatko.university.domain.Participant;
import com.skorobahatko.university.domain.Timetable;

public class DatabaseTestUtils {
	
	private static String databaseUrl = "jdbc:postgresql://127.0.0.1:5432/university?user=university_admin&password=admin";
	private static DataSource dataSource;
	
	public static DataSource getTestDataSource() {
		if (dataSource == null) {
			PGSimpleDataSource postgresDataSource = new PGSimpleDataSource();
			postgresDataSource.setUrl(databaseUrl);
			dataSource = postgresDataSource;
		}
		
		return dataSource;
	}
	
	public static void initializeTestDatabase() throws Exception {
		Path sqlFile = Path.of(DatabaseTestUtils.class.getClassLoader().getResource("create_tables.sql").toURI());
		String initDatabaseSql = Files.readString(sqlFile);
		
		execute(initDatabaseSql);
		
	}
	
	public static void insertCourseToTheTestDatabase(Course course) throws SQLException {
		String insertCourseSql = "INSERT INTO courses (course_id, course_name) VALUES (?, ?);";
		
		ValidatedValue[] values = {
				ValidatedValue.of(Types.INTEGER, course.getId()),
				ValidatedValue.of(Types.VARCHAR, course.getName())
		};
		
		execute(insertCourseSql, values);
	}
	
	public static void insertLecturesToTheTestDatabase(List<Lecture> lectures) throws SQLException {
		String sql = "INSERT INTO lectures "
				+ "(lecture_id, course_id, lecture_name, lecture_date, lecture_start_time, lecture_end_time, lecture_room_number) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?);";
		
		try (Connection connection = DriverManager.getConnection(databaseUrl);
				PreparedStatement statement = connection.prepareStatement(sql)) {
			
			for (Lecture lecture : lectures) {
				statement.setInt(1, lecture.getId());
				statement.setInt(2, lecture.getCourseId());
				statement.setString(3, lecture.getName());
				statement.setDate(4, Date.valueOf(lecture.getDate()));
				statement.setTime(5, Time.valueOf(lecture.getStartTime()));
				statement.setTime(6, Time.valueOf(lecture.getEndTime()));
				statement.setInt(7, lecture.getRoomNumber());
				
				statement.addBatch();
			}
			
			statement.executeBatch();	
		}
	}
	
	public static void insertParticipantRole(int roleId, String roleName) throws SQLException {
		String insertParticipantRoleSql = "INSERT INTO participant_roles (role_id, role_name) VALUES (?, ?);";
		
		ValidatedValue[] values = {
				ValidatedValue.of(Types.INTEGER, roleId),
				ValidatedValue.of(Types.VARCHAR, roleName)
		};
		
		execute(insertParticipantRoleSql, values);
	}
	
	public static void insertParticipantToTheTestDatabase(Participant participant) throws SQLException {
		int roleId = 1;
		String roleName = "Student";
		insertParticipantRole(roleId, roleName);
		
		String insertParticipantSql = "INSERT INTO participants (participant_id, first_name, last_name, role_id) "
				+ "VALUES (?, ?, ?, ?);";
		
		ValidatedValue[] values = new ValidatedValue[] {
				ValidatedValue.of(Types.INTEGER, participant.getId()),
				ValidatedValue.of(Types.VARCHAR, participant.getFirstName()),
				ValidatedValue.of(Types.VARCHAR, participant.getLastName()),
				ValidatedValue.of(Types.INTEGER, roleId)
		};
		
		execute(insertParticipantSql, values);
		
		if (!participant.getCourses().isEmpty()) {
			String insertParticipantCoursePairSql = "INSERT INTO participants_courses "
					+ "(participant_id, course_id) VALUES (?, ?);";
			
			List<Course> courses = participant.getCourses();
			for (Course course : courses) {
				values = new ValidatedValue[] {
						ValidatedValue.of(Types.INTEGER, participant.getId()),
						ValidatedValue.of(Types.INTEGER, course.getId())
				};
				
				execute(insertParticipantCoursePairSql, values);
			}
			
		}
	}
	
	public static void insertTimetableToTheTestDatabase(Timetable timetable) throws SQLException {
		String insertTimetableSql = "INSERT INTO timetables "
				+ "(timetable_id, participant_id, timetable_start_date, timetable_end_date) "
				+ "VALUES (?, ?, ?, ?)";
		
		ValidatedValue[] values = {
				ValidatedValue.of(Types.INTEGER, timetable.getId()),
				ValidatedValue.of(Types.INTEGER, timetable.getParticipant().getId()),
				ValidatedValue.of(Types.DATE, Date.valueOf(timetable.getStartDate())),
				ValidatedValue.of(Types.DATE, Date.valueOf(timetable.getEndDate()))
		};
		
		execute(insertTimetableSql, values);
	}
	
	private static void execute(String sql, ValidatedValue... values) throws SQLException {
		try (Connection connection = DriverManager.getConnection(databaseUrl);
				PreparedStatement statement = connection.prepareStatement(sql)) {
			
			for (int i = 0; i < values.length; i++) {
				int numberOfSqlParameter = i + 1;
				Object value = values[i].getValue();
				int type = values[i].getType();
				statement.setObject(numberOfSqlParameter, value, type);
			}
			
			statement.executeUpdate();
		}
	}
	
	private static class ValidatedValue {

		private int type;
		private Object value;

		static ValidatedValue of(int type, Object value) {
			return new ValidatedValue(type, value);
		}

		private ValidatedValue(int type, Object value) {
			this.type = type;
			this.value = value;
		}

		int getType() {
			return type;
		}

		Object getValue() {
			return value;
		}

	}

}
