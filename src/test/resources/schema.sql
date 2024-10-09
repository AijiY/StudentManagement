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

CREATE TABLE courses (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    price INT
);

CREATE TABLE student_courses (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    start_date DATE,
    end_due_date DATE,
    course_id INT NOT NULL,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);

CREATE TABLE student_course_statuses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_course_id INT NOT NULL UNIQUE,
    status VARCHAR(10) NOT NULL,
    FOREIGN KEY (student_course_id) REFERENCES student_courses(id) ON DELETE CASCADE
);
