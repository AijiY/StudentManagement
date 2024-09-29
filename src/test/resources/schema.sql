CREATE TABLE students (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    kana_name VARCHAR(50) NOT NULL,
    nickname VARCHAR(10),
    email VARCHAR(50) NOT NULL,
    living_area VARCHAR(50),
    age INT,
    gender VARCHAR(10),
    remark VARCHAR(100),
    deleted TINYINT DEFAULT FALSE
);

CREATE TABLE student_courses (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    start_date DATE,
    end_due_date DATE,
    course_id INT NOT NULL
);

CREATE TABLE courses (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    price INT
);