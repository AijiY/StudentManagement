package raisetech.student.management.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.student.management.data.Course;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.exception.ResourceConflictException;
import raisetech.student.management.exception.ResourceNotFoundException;
import raisetech.student.management.repository.StudentRepository;
import raisetech.student.management.service.converter.StudentConverter;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  private StudentService sut;
  @BeforeEach
  void setup() {
    sut = new StudentService(repository, converter);
  }

  @Test
  void 受講生情報の一覧検索_リポジトリの処理が適切に呼び出せること() {
    // 事前準備（ここではsutのみ）
    // 実行
    List<Student> actual = sut.searchStudents();
    // 検証
    Mockito.verify(repository, Mockito.times(1)).searchStudents();
  }

  @Test
  void 受講生コース情報の一覧検索_リポジトリの処理が適切に呼び出せること() throws ResourceNotFoundException {
    // 事前準備（ここではsutのみ）
    // 実行
    List<StudentCourse> actual = sut.searchStudentCourses();
    // 検証
    Mockito.verify(repository, Mockito.times(1)).searchStudentCourses();
  }

  @Test
  void 受講生情報のID検索_リポジトリの処理が適切に呼び出せること() throws ResourceNotFoundException {
    // 事前準備
    int id = 1;
    Student student = new Student(id, null, null, null, null, null, 1, null, null, false);
    Mockito.when(repository.searchStudentById(id)).thenReturn(Optional.of(student));
    // 実行
    Student actual = sut.searchStudentById(id);
    // リポジトリの呼び出しを検証
    Mockito.verify(repository, Mockito.times(1)).searchStudentById(id);
  }

  @Test
  void 受講生情報のID検索_指定されたIDの受講生が存在しない場合に例外が発生すること() {
    // 事前準備
    int id = 1;
    Mockito.when(repository.searchStudentById(id)).thenReturn(Optional.empty());
    // 例外処理の発生を検証
    assertThrows(ResourceNotFoundException.class, () -> sut.searchStudentById(id));
  }

  @Test
  void 受講生コース情報を受講生IDから検索_リポジトリの処理が適切に呼び出せること() throws ResourceNotFoundException {
    // 事前準備（ここではsutのみ）
    // 実行
    List<StudentCourse> actual = sut.searchStudentCoursesByStudentId(1);
    // 検証
    Mockito.verify(repository, Mockito.times(1)).searchStudentCoursesByStudentId(1);
  }

  @Test
  void 受講生詳細情報を全件検索_リポジトリとコンバーターの処理が適切に呼び出せること() throws ResourceNotFoundException {
    // 事前準備
    List<Student> students = new ArrayList<>();
    List<StudentCourse> studentCourses = new ArrayList<>();
    Mockito.when(repository.searchStudents()).thenReturn(students);
    Mockito.when(repository.searchStudentCourses()).thenReturn(studentCourses);
    // 実行
    List<StudentDetail> actual = sut.searchStudentDetails();
    // 検証
    Mockito.verify(repository, Mockito.times(1)).searchStudents();
    Mockito.verify(repository, Mockito.times(1)).searchStudentCourses();
    Mockito.verify(converter, Mockito.times(1)).convertStudentDetails(students, studentCourses);
  }

  @Test
  void 受講生IDを指定して受講生詳細情報を検索_リポジトリの処理が適切に呼び出せること() throws ResourceNotFoundException {
    // 事前準備
    int id = 1;
    Student student = new Student(id, null, null, null, null, null, 1, null, null, false);
    List<StudentCourse> studentCourses = new ArrayList<>();
    Mockito.when(repository.searchStudentById(id)).thenReturn(Optional.of(student));
    Mockito.when(repository.searchStudentCoursesByStudentId(id)).thenReturn(studentCourses);
    // 実行
    StudentDetail actual = sut.searchStudentDetailById(id);
    // 検証
    Mockito.verify(repository, Mockito.times(1)).searchStudentById(id);
    Mockito.verify(repository, Mockito.times(1)).searchStudentCoursesByStudentId(id);
  }

  @Test
  void コース情報を全件検索_リポジトリの処理が適切に呼び出せること() {
    // 事前準備（ここではsutのみ）
    // 実行
    List<Course> actual = sut.searchCourses();
    // 検証
    Mockito.verify(repository, Mockito.times(1)).searchCourses();
  }

  @Test
  void コースIDを指定してコース名を検索_リポジトリの処理が適切に呼び出せること() throws ResourceNotFoundException {
    // 事前準備
    String name = "サンプルコース";
    Mockito.when(repository.searchCourseNameById(1)).thenReturn(Optional.of(name));
    // 実行
    String actual = sut.searchCourseNameById(1);
    // 検証
    Mockito.verify(repository, Mockito.times(1)).searchCourseNameById(1);
  }

  @Test
  void コースIDを指定してコース名を検索_指定されたIDのコースが存在しない場合に例外が発生すること() {
    // 事前準備
    int id = 1;
    Mockito.when(repository.searchCourseNameById(id)).thenReturn(Optional.empty());
    // 例外処理の発生を検証
    assertThrows(ResourceNotFoundException.class, () -> sut.searchCourseNameById(id));
  }

  @Test
  void 受講生情報の新規登録_リポジトリの処理が適切に呼び出せること() throws ResourceNotFoundException {
    // 事前準備
    Student student = new Student(0, null, null, null, null, null, 1, null, null, false);
    List<StudentCourse> studentCourses = new ArrayList<>();
    studentCourses.add(StudentCourse.initStudentCourse(0, 1));
    StudentDetail studentDetail = new StudentDetail(student, studentCourses);
    // repositoryをmock化しているため、studentCourseに登録されたstudentIdが0のまま
    Mockito.when(repository.searchStudentById(0)).thenReturn(Optional.of(student));
    Mockito.when(repository.searchCourseNameById(1)).thenReturn(Optional.of("サンプルコース"));
    // 実行
    sut.registerStudent(studentDetail);
    // 検証
    Mockito.verify(repository, Mockito.times(1)).insertStudent(student);
  }

  @Test
  void 受講生情報の更新_リポジトリの処理が適切に呼び出せること() {
    // 事前準備
    Student student = new Student(1, null, null, null, null, null, 1, null, null, false);
    List<StudentCourse> studentCourses = new ArrayList<>();
    studentCourses.add(StudentCourse.initStudentCourse(1, 1));
    StudentDetail studentDetail = new StudentDetail(student, studentCourses);
    // 実行
    sut.updateStudent(studentDetail);
    // 検証
    Mockito.verify(repository, Mockito.times(1)).updateStudent(student);
  }

  @Test
  void 受講生コース情報の新規登録_リポジトリの処理が適切に呼び出せること() throws ResourceNotFoundException {
    // 事前準備
    StudentCourse studentCourse = StudentCourse.initStudentCourse(1, 1);
    Mockito.when(repository.searchCourseNameById(1)).thenReturn(Optional.of("サンプルコース"));
    Mockito.when(repository.searchStudentById(1))
        .thenReturn(Optional.of(new Student(1, null, null, null, null, null, 1, null, null, false)));
    // 実行
    sut.registerStudentCourse(studentCourse);
    // 検証
    Mockito.verify(repository, Mockito.times(1)).insertStudentCourse(studentCourse);
  }

  @Test
  void コース情報の新規登録_リポジトリの処理が適切に呼び出せること() {
    // 事前準備
    Course course = new Course(0, null, 0);
    // 実行
    sut.registerCourse(course);
    // 検証
    Mockito.verify(repository, Mockito.times(1)).insertCourse(course);
  }

  @Test
  void 受講生情報の論理削除_リポジトリの処理が適切に呼び出せること() throws ResourceConflictException, ResourceNotFoundException {
    // 事前準備
    int id = 1;
    Student student = new Student(id, null, null, null, null, null, 1, null, null, false);
    Mockito.when(repository.searchStudentById(id)).thenReturn(Optional.of(student));
    // 実行
    sut.deleteStudent(id);
    // 検証
    Mockito.verify(repository, Mockito.times(1)).searchStudentById(id);
    Mockito.verify(repository, Mockito.times(1)).deleteStudent(id);
  }

  @Test
  void 受講生情報の論理削除_指定されたIDの受講生が存在しない場合に例外が発生すること() throws ResourceConflictException, ResourceNotFoundException {
    // 事前準備
    int id = 1;
    Mockito.when(repository.searchStudentById(id)).thenReturn(Optional.empty());
    // 例外処理の発生を検証
    assertThrows(ResourceNotFoundException.class, () -> sut.deleteStudent(id));
  }

  @Test
  void 受講生情報の論理削除_指定されたIDの受講生が削除済みの場合に例外が発生すること() throws ResourceConflictException, ResourceNotFoundException {
    // 事前準備
    int id = 1;
    Student student = new Student(id, null, null, null, null, null, 1, null, null, true);
    Mockito.when(repository.searchStudentById(id)).thenReturn(Optional.of(student));
    // 例外処理の発生を検証
    assertThrows(ResourceConflictException.class, () -> sut.deleteStudent(id));
  }
}
