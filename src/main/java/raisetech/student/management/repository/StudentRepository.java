package raisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;

/**
 * 受講生情報を扱うリポジトリ
 *
 * 全権検索や単一検索、コース情報の検索が行える
 */
@Mapper
public interface StudentRepository {
  @Select("SELECT * FROM students "
      + "WHERE is_deleted = 0 "
      + "ORDER BY id")
  List<Student> searchStudent();

  @Select("SELECT * FROM students_courses")
  List<StudentCourse> searchStudentCourse();

//  もっとも大きいidを取得
  @Select("SELECT MAX(id) FROM students")
  int searchMaxStudentId();

  @Select("SELECT MAX(id) FROM students_courses")
  int searchMaxCourseId();

//  受講生名からidを取得
  @Select("SELECT id FROM students WHERE name = #{name}")
  int searchStudentIdByName(String name);

  @Insert("INSERT INTO students VALUES (#{id}, #{name}, #{kanaName}, #{nickname}, #{email}, #{livingArea}, #{age}, #{gender}, #{remark}, false)")
  void insertStudent(Student student);

  @Insert("INSERT INTO students_courses VALUES (#{id}, #{studentId}, #{courseName}, #{startDate}, #{endDueDate})")
  void insertStudentCourse(StudentCourse studentCourse);
}

