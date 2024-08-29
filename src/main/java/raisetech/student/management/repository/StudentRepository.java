package raisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;

/**
 * 受講生情報を扱うリポジトリ
 *
 * 検索や登録、更新などを行う
 */
@Mapper
public interface StudentRepository {

  /**
   * 受講生情報を全件検索（論理削除されているものは除外）
   * @return 受講生情報一覧
   */
  @Select("SELECT * FROM students "
      + "WHERE deleted = 0 "
      + "ORDER BY id")
  List<Student> searchStudent();

  /**
   * 受講生のコース情報を全件検索
   * @return 受講生のコース情報一覧
   */
  @Select("SELECT * FROM students_courses")
  List<StudentCourse> searchStudentCourse();

  /**
   * 受講生IDを指定して受講生情報を検索
   * @param id
   * @return
   */
  @Select("SELECT * FROM students WHERE id = #{id}")
  Student searchStudentById(int id);

  /**
   * 受講生IDを指定して受講生のコース情報を検索
   * @param studentId
   * @return
   */
  @Select("SELECT * FROM students_courses WHERE student_id = #{studentId}")
  List<StudentCourse> searchStudentCoursesByStudentId(int studentId);

  /**
   * 受講生情報を新規登録
   * @param student
   */
  @Insert("INSERT INTO students VALUES (#{id}, #{name}, #{kanaName}, #{nickname}, #{email}, #{livingArea}, #{age}, #{gender}, #{remark}, #{deleted})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertStudent(Student student);

  /**
   * 受講生のコース情報を新規登録
   * @param studentCourse
   */
  @Insert("INSERT INTO students_courses VALUES (#{id}, #{studentId}, #{courseName}, #{startDate}, #{endDueDate})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertStudentCourse(StudentCourse studentCourse);

  /**
   * 受講生情報を更新
   * @param student
   */
  @Update("UPDATE students SET name = #{name}, kana_name = #{kanaName}, nickname = #{nickname}, email = #{email}, "
      + "living_area = #{livingArea}, age = #{age}, gender = #{gender}, remark = #{remark}, deleted = #{deleted} WHERE id = #{id}")
  void updateStudent(Student student);
}

