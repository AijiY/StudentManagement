INSERT INTO students (name, kana_name, nickname, email, living_area, age, gender, remark, deleted) VALUES
('山田太郎', 'やまだたろう', 'たろう', 'taro.yamada@example.com', '東京都新宿区', 20, '男性', '', 0),
('鈴木花子', 'すずきはなこ', 'はな', 'hanako.suzuki@example.com', '大阪府大阪市', 22, '女性', '', 0),
('佐藤健', 'さとうたける', 'たけ', 'take.sato@example.com', '愛知県名古屋市', 21, '男性', '', 0),
('高橋美咲', 'たかはしみさき', 'みさき', 'misaki.takahashi@example.com', '福岡県福岡市', 23, '女性', '', 0),
('田中大輔', 'たなかだいすけ', 'だいすけ', 'daisuke.tanaka@example.com', '北海道札幌市', 24, '男性', '', 1);


-- Insert data into the courses table
INSERT INTO courses (name, price) VALUES
('Javaコース', 200000),
('PHPコース', 180000),
('Rubyコース', 150000),
('Pythonコース', 250000);

-- Insert data into the student_courses table
INSERT INTO student_courses (student_id, start_date, end_due_date, course_id) VALUES
(1, '2024-01-01', '2024-04-25', 1),
(1, '2024-04-26', '2024-08-25', 2),
(2, '2024-01-01', '2024-04-25', 3),
(3, '2024-01-01', '2024-04-25', 1),
(4, '2024-01-01', '2024-04-25', 2),
(5, '2024-01-01', '2024-04-25', 4);

-- Insert data into the student_course_statuses table
INSERT INTO student_course_statuses (student_course_id, status) VALUES
(1, '受講中'),
(2, '受講中'),
(3, '受講中'),
(4, '受講中'),
(5, '仮申し込み'),
(6, '完了');
