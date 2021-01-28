INSERT INTO participants (first_name, last_name, role) VALUES
	('John', 'Johnson', 'Teacher'),
	('Harry', 'Harrison', 'Teacher'),
	('Gary', 'Morgan', 'Teacher'),
	('Jack', 'Jackson', 'Student'),
	('Tom', 'Tompson', 'Student'),
	('Andrew', 'Anderson', 'Student'),
	('Dave', 'Daveson', 'Student'),
	('Steven', 'Stevenson', 'Student');

INSERT INTO participants_courses (participant_id, course_id) VALUES 
	(1, 1),
	(1, 2),
	(2, 3),
	(3, 2),
	(4, 1),
	(4, 3),
	(5, 2),
	(6, 1),
	(6, 2),
	(7, 2),
	(7, 3),
	(8, 1);
