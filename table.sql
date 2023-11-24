CREATE TABLE Students(
	student_id  serial Primary Key,
	first_name text NOT NULL,
	last_name text NOT NULL,
	email text NOT NULL UNIQUE,
	enrollment_date date
);
