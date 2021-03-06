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

DROP TABLE IF EXISTS participants CASCADE;
CREATE TABLE participants
(
	participant_id SERiAL PRIMARY KEY,
	first_name CHARACTER VARYING(100) NOT NULL,
	last_name CHARACTER VARYING(100) NOT NULL,
	role CHARACTER VARYING(30) NOT NULL
);

DROP TABLE IF EXISTS participants_courses CASCADE;
CREATE TABLE participants_courses
(
  	participant_id INTEGER REFERENCES participants (participant_id) ON DELETE CASCADE,
	course_id INTEGER REFERENCES courses (course_id) ON DELETE CASCADE,
  	PRIMARY KEY (participant_id, course_id)
);