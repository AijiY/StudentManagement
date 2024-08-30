package raisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import raisetech.student.management.data.Course;
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
  List<Student> searchStudents();

  /**
   * 受講生のコース情報を全件検索
   * @return 受講生のコース情報一覧
   */
  List<StudentCourse> searchStudentCourses();

  /**
   * 受講生IDを指定して受講生情報を検索
   * @param id
   * @return
   */
  Student searchStudentById(int id);

  /**
   * 受講生IDを指定して受講生のコース情報を検索
   * @param studentId
   * @return
   */
  List<StudentCourse> searchStudentCoursesByStudentId(int studentId);

  /**
   * コース情報を全件検索
   * @return
   */
  List<Course> searchCourses();

  /**
   * コースIDを指定してコース名を検索
   * @param id
   * @return
   */
  String searchCourseNameById(int id);

  /**
   * 受講生情報を新規登録
   * @param student
   */
  void insertStudent(Student student);

  /**
   * 受講生のコース情報を新規登録
   * @param studentCourse
   */
  void insertStudentCourse(StudentCourse studentCourse);

  /**
   * コース情報を新規登録
   * @param course
   */
  void insertCourse(Course course);

  /**
   * 受講生情報を更新
   * @param student
   */
  void updateStudent(Student student);

  /**
   * 受講生情報を論理削除
   * @param id
   */
  void deleteStudent(int id);
}

