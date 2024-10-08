package raisetech.student.management.service.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.StudentCourseStatus;
import raisetech.student.management.domain.StudentDetail;

class StudentConverterTest {

  private StudentConverter sut;
  @BeforeEach
  void setup() {
    sut = new StudentConverter();
  }

  @Test
  void 受講生詳細の変換で対象の受講生情報がすべてマッピングされること() {
    // 事前準備
    List<Student> students = new ArrayList<>(List.of(
        new Student(1, "AAA", "aaa", null, "aaa@example.com", null, 1, null, null, false),
        new Student(2, "BBB", "bbb", null, "bbb@example.com", null, 2, null, null, false)
    ));
    List<StudentCourse> studentCourses = new ArrayList<>();
    // 実行
    List<StudentDetail> actual = sut.convertStudentDetails(students, studentCourses);
    // 検証
    // actualの要素数がstudentsの要素数と等しいこと
    assertThat(actual.size()).isEqualTo(students.size());
  }

  @Test
  void 受講生詳細の変換でStudentのidとStudentCourseのstudentIdが一致する情報がマッピングされること() {
    // 事前準備
    List<Student> students = new ArrayList<>(List.of(
        new Student(1, "AAA", "aaa", null, "aaa@example.com", null, 1, null, null, false)
    ));
    List<StudentCourse> studentCourses = new ArrayList<>(List.of(
        new StudentCourse(1, 1, null, null, 1),
        new StudentCourse(2, 1, null, null, 2),
        new StudentCourse(3, 3, null, null, 1)
    ));
    // 実行
    List<StudentDetail> actual = sut.convertStudentDetails(students, studentCourses);
    // 検証
    // actualのid=1の受講生のコース情報の件数が適切であること
    StudentDetail studentDetailStudentId1 = actual.stream()
        .filter(studentDetail -> studentDetail.getStudent().getId() == 1)
        .findFirst()
        .orElseThrow();
    int CountStudentCourseStudentId1 = (int) studentCourses.stream()
        .filter(studentCourse -> studentCourse.getStudentId() == 1)
        .count();
    assertThat(studentDetailStudentId1.getStudentCourses().size()).isEqualTo(
        CountStudentCourseStudentId1);
  }

  @Test
  void 受講生詳細の変換で対応するstudentIdのStudentCourseが存在しない場合はStudentCourseが空であること() {
    // 事前準備
    List<Student> students = new ArrayList<>(List.of(
        new Student(2, "BBB", "bbb", null, "bbb@example.com", null, 2, null, null, false)
    ));
    List<StudentCourse> studentCourses = new ArrayList<>(List.of(
        new StudentCourse(1, 1, null, null, 1),
        new StudentCourse(2, 1, null, null, 2),
        new StudentCourse(3, 3, null, null, 1)
    ));
    // 実行
    List<StudentDetail> actual = sut.convertStudentDetails(students, studentCourses);
    // 検証
    // actualのid=2の受講生のコース情報が0件であること
    StudentDetail studentDetailStudentId2 = actual.stream()
        .filter(studentDetail -> studentDetail.getStudent().getId() == 2)
        .findFirst()
        .orElseThrow();
    assertThat(studentDetailStudentId2.getStudentCourses().size()).isEqualTo(0);
  }

  @Test
  void 受講生詳細の変換でstudentsに存在しないidをstudentIdとして持つStudentCourseはマッピングされないこと() {
    // 事前準備
    List<Student> students = new ArrayList<>(List.of(
        new Student(1, "AAA", "aaa", null, "aaa@example.com", null, 1, null, null, false),
        new Student(2, "BBB", "bbb", null, "bbb@example.com", null, 2, null, null, false)
    ));
    List<StudentCourse> studentCourses = new ArrayList<>(List.of(
        new StudentCourse(1, 1, null, null, 1),
        new StudentCourse(2, 1, null, null, 2),
        new StudentCourse(3, 3, null, null, 1)
    ));
    // 実行
    List<StudentDetail> actual = sut.convertStudentDetails(students, studentCourses);
    // 検証
    // actualにid=3(StudentId=3)のStudentCourseが存在しないこと
    List<StudentCourse> actualStudentCourses = actual.stream()
        .flatMap(studentDetail -> studentDetail.getStudentCourses().stream())
        .toList();
    assertThat(actualStudentCourses.stream()
        .anyMatch(studentCourse -> studentCourse.getId() == 3)).isFalse();
  }

  @Test
  void 申し込み状況に該当するものに限定して受講生詳細の変換でStudentCourseStatusに含まれるStudentCourseのみマッピングされること() {
    // 事前準備
    List<Student> students = new ArrayList<>(List.of(
        new Student(1, "AAA", "aaa", null, "aaa@example.com", null, 1, null, null, false),
        new Student(2, "BBB", "bbb", null, "bbb@example.com", null, 2, null, null, false)
    ));
    List<StudentCourse> studentCourses = new ArrayList<>(List.of(
        new StudentCourse(1, 1, null, null, 1),
        new StudentCourse(2, 1, null, null, 2),
        new StudentCourse(3, 2, null, null, 1)
    ));
    List<StudentCourseStatus> studentCourseStatuses = new ArrayList<>(List.of(
        new StudentCourseStatus(2, 2, "受講中")
    ));
    // 実行
    List<StudentDetail> actual = sut.convertStudentDetailsWithStatus(students, studentCourses,
        studentCourseStatuses);
    // 検証
    // actualのサイズが1であること
    assertThat(actual.size()).isEqualTo(1);
    // actualのStudentIdが1であること
    assertThat(actual.get(0).getStudent().getId()).isEqualTo(1);
    // actualのStudentCourseのサイズが1であること
    assertThat(actual.get(0).getStudentCourses().size()).isEqualTo(1);
    // actualのStudentCourseのIdが2であること
    assertThat(actual.get(0).getStudentCourses().get(0).getId()).isEqualTo(2);
  }

  @Test
  void 申し込み状況に該当するものに限定して受講生詳細の変換でStudentCourseStatusが空である場合はからのListが返されること() {
    // 事前準備
    List<Student> students = new ArrayList<>(List.of(
        new Student(1, "AAA", "aaa", null, "aaa@example.com", null, 1, null, null, false),
        new Student(2, "BBB", "bbb", null, "bbb@example.com", null, 2, null, null, false)
    ));
    List<StudentCourse> studentCourses = new ArrayList<>(List.of(
        new StudentCourse(1, 1, null, null, 1),
        new StudentCourse(2, 1, null, null, 2),
        new StudentCourse(3, 2, null, null, 1)
    ));
    List<StudentCourseStatus> studentCourseStatuses = new ArrayList<>();
    // 実行
    List<StudentDetail> actual = sut.convertStudentDetailsWithStatus(students, studentCourses,
        studentCourseStatuses);
    // 検証
    // actualが空であること
    assertThat(actual.size()).isEqualTo(0);
  }
}
