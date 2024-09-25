package raisetech.student.management.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.student.management.data.Course;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.CourseForJson;
import raisetech.student.management.domain.StudentDetailForJson;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  @Test
  void 受講生情報の全件検索が実施できること_deletedがfalseの受講生数が適切であること() {
    List<Student> actual = sut.searchStudents();
    assertThat(actual.size()).isEqualTo(4);
  }

  @Test
  void 受講生のコース情報の全件検索が実施できること_件数が適切であること() {
    List<StudentCourse> actual = sut.searchStudentCourses();
    assertThat(actual.size()).isEqualTo(6);
  }

  @Test
  void 受講生IDを指定して受講生情報を検索が実施できること_指定したIDの受講生情報が取得できること() {
    Student actual = sut.searchStudentById(1);
    assertThat(actual.getId()).isEqualTo(1);
  }

  @Test
  void 受講生IDを指定して受講生のコース情報を検索が実施できること_指定したIDの受講生のコース情報が取得できること() {
    List<StudentCourse> actual = sut.searchStudentCoursesByStudentId(1);
    assertThat(actual.size()).isEqualTo(2);
  }

  @Test
  void コース情報の全件検索が実施できること_件数が適切であること() {
    List<Course> actual = sut.searchCourses();
    assertThat(actual.size()).isEqualTo(4);
  }

  @Test
  void コースIDを指定してコース名を検索が実施できること_指定したIDのコース名が取得できること() {
    String actual = sut.searchCourseNameById(1);
    assertThat(actual).isEqualTo("Javaコース");
  }

  @Test
  void 受講生情報の新規登録が実施できること_登録前後でstudents件数が1件増えていること() {
    StudentDetailForJson studentDetailForJson = new StudentDetailForJson(
        "テスト太郎", "テストタロウ", "テスト", "test@example.com",
        "テスト県テスト市", 20, "男性", null, 1
    );
    Student student = new Student(studentDetailForJson);
    int countPresentStudents = sut.searchStudents().size();
    sut.insertStudent(student);
    List<Student> actual = sut.searchStudents();
    assertThat(actual.size()).isEqualTo(countPresentStudents + 1);
  }

  @Test
  void 受講生のコース情報の新規登録が実施できること_登録前後でstudent_courses件数が1件増えていること() {
    StudentCourse studentCourse = StudentCourse.initStudentCourse(1, 3);
    int countPresentStudentCourses = sut.searchStudentCourses().size();
    sut.insertStudentCourse(studentCourse);
    List<StudentCourse> actual = sut.searchStudentCourses();
    assertThat(actual.size()).isEqualTo(countPresentStudentCourses + 1);
  }

  @Test
  void コース情報の新規登録が実施できること_登録前後でcourses件数が1件増えていること() {
    CourseForJson courseForJson = new CourseForJson("テストコース", 100000);
    Course course = new Course(courseForJson);
    int countPresentCourses = sut.searchCourses().size();
    sut.insertCourse(course);
    List<Course> actual = sut.searchCourses();
    assertThat(actual.size()).isEqualTo(countPresentCourses + 1);
  }

  @Test
  void 受講生情報の更新が実施できること_更新に使用した情報と更新後の情報が一致していること() {
    Student student = sut.searchStudentById(1);
    // 元の情報を保存
    Student original = student;
    // nicknameを変更
    student.setNickname("テストニックネーム");
    // 更新
    sut.updateStudent(student);
    Student actual = sut.searchStudentById(1);
    // nicknameが変更されていることを確認
    assertThat(actual.getNickname()).isEqualTo("テストニックネーム");
    // 他の情報は変更されていないことを確認（代表としてnameのみ）
    assertThat(actual.getName()).isEqualTo(original.getName());
  }

  @Test
  void 受講生情報の論理削除が実施できること_削除フラグがtrueになっていること() {
    sut.deleteStudent(1);
    Student actual = sut.searchStudentById(1);
    assertThat(actual.isDeleted()).isTrue();
  }
}
