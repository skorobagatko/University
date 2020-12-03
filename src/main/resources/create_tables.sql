DROP TABLE IF EXISTS courses CASCADE;
CREATE TABLE courses
(
	course_id SERIAL PRIMARY KEY,
	course_name CHARACTER VARYING(100) NOT NULL
);

DROP TABLE IF EXISTS lectures CASCADE;
CREATE TABLE lectures
(
	lecture_id SERIAL PRIMARY KEY,
	course_id INTEGER NOT NULL,
	lecture_name CHARACTER VARYING(100) NOT NULL,
	lecture_date DATE NOT NULL,
	lecture_start_time TIME NOT NULL,
	lecture_end_time TIME NOT NULL,
	lecture_room_number INTEGER NOT NULL,
	FOREIGN KEY (course_id)
    	REFERENCES courses (course_id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS participant_roles CASCADE;
CREATE TABLE participant_roles
(
	role_id SERiAL PRIMARY KEY,
	role_name CHARACTER VARYING(100) NOT NULL
);

DROP TABLE IF EXISTS participants CASCADE;
CREATE TABLE participants
(
	participant_id SERiAL PRIMARY KEY,
	first_name CHARACTER VARYING(100) NOT NULL,
	last_name CHARACTER VARYING(100) NOT NULL,
	role_id INTEGER DEFAULT NULL,
	FOREIGN KEY (role_id)
		REFERENCES participant_roles (role_id) ON DELETE SET DEFAULT
);

DROP TABLE IF EXISTS participants_courses CASCADE;
CREATE TABLE participants_courses
(
  	participant_id INTEGER REFERENCES participants (participant_id) ON DELETE CASCADE,
	course_id INTEGER REFERENCES courses (course_id) ON DELETE CASCADE,
  	PRIMARY KEY (participant_id, course_id)
);

DROP TABLE IF EXISTS timetables CASCADE;
CREATE TABLE timetables
(
	timetable_id SERIAL PRIMARY KEY,
	participant_id INTEGER NOT NULL,
	timetable_start_date DATE NOT NULL,
	timetable_end_date DATE NOT NULL,
	FOREIGN KEY (participant_id)
		REFERENCES participants (participant_id) ON DELETE CASCADE
);
