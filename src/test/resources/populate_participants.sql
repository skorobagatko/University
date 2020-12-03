INSERT INTO participant_roles (role_id, role_name) VALUES
	(1, 'Teacher'),
	(2, 'Student');

INSERT INTO participants (first_name, last_name, role_id) VALUES
	('John', 'Johnson', 1),
	('Harry', 'Harrison', 1),
	('Gary', 'Morgan', 1),
	('Jack', 'Jackson', 2),
	('Tom', 'Tompson', 2),
	('Andrew', 'Anderson', 2),
	('Dave', 'Daveson', 2),
	('Steven', 'Stevenson', 2);

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
