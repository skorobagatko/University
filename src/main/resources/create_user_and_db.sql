CREATE DATABASE university;
CREATE USER university_admin WITH ENCRYPTED PASSWORD 'admin';
GRANT ALL PRIVILEGES ON DATABASE university TO university_admin;