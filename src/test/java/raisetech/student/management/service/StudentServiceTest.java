package raisetech.student.management.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
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
import raisetech.student.management.data.StudentCourseStatus;
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
    Mockito.verify(repository, Mockito.times(1)).insertStudentCourseStatus(Mockito.any(StudentCourseStatus.class));
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

  @Test
  void 受講生コース申し込み状況の受講中への更新_リポジトリの処理が適切に呼び出せること() throws ResourceNotFoundException, ResourceConflictException {
    // privateメソッドの部分も同時にテスト
    // 事前準備
    int studentCourseId = 1;
    StudentCourseStatus studentCourseStatus = new StudentCourseStatus(1, studentCourseId, "仮申し込み") ;
    Mockito.when(repository.searchStudentCourseStatusByStudentCourseId(1)).thenReturn(Optional.of(studentCourseStatus));
    StudentCourse studentCourse = new StudentCourse(studentCourseId, 1, LocalDate.now(), LocalDate.now().plusWeeks(16), 1);
    Mockito.when(repository.searchStudentCourseById(1)).thenReturn(Optional.of(studentCourse));
    // 実行
    sut.updateStudentCourseStatusInProgress(studentCourseId);
    // 検証
    Mockito.verify(repository, Mockito.times(1)).searchStudentCourseStatusByStudentCourseId(studentCourseId);
    Mockito.verify(repository, Mockito.times(1)).updateStudentCourseStatusInProgress(studentCourseId);
    Mockito.verify(repository, Mockito.times(1)).searchStudentCourseById(studentCourseId);
    Mockito.verify(repository, Mockito.times(1)).updateStudentCourse(studentCourse);
  }

  @Test
  void 受講生コース申し込み状況の受講中への更新_指定されたIDの受講生コース申し込み状況が存在しない場合に例外が発生すること() {
    // 事前準備
    int studentCourseId = 1;
    Mockito.when(repository.searchStudentCourseStatusByStudentCourseId(1)).thenReturn(Optional.empty());
    // 例外処理の発生を検証
    assertThrows(ResourceNotFoundException.class, () -> sut.updateStudentCourseStatusInProgress(studentCourseId));
  }

  @Test
  void 受講生コース申し込み状況の受講中への更新_指定されたIDの受講生コースが既に受講中の場合に例外が発生すること() {
    // 事前準備
    int studentCourseId = 1;
    StudentCourseStatus studentCourseStatus = new StudentCourseStatus(1, studentCourseId, "受講中") ;
    Mockito.when(repository.searchStudentCourseStatusByStudentCourseId(1)).thenReturn(Optional.of(studentCourseStatus));
    // 例外処理の発生を検証（メッセージ含む）
    ResourceConflictException exception = assertThrows(ResourceConflictException.class, () -> sut.updateStudentCourseStatusInProgress(studentCourseId));
    assert(exception.getMessage().contains("既に受講中の受講生コースです"));
  }

  @Test
  void 受講生コース申し込み状況の受講中への更新_指定されたIDの受講生コースが完了の場合に例外が発生すること() {
    // 事前準備
    int studentCourseId = 1;
    StudentCourseStatus studentCourseStatus = new StudentCourseStatus(1, studentCourseId, "完了") ;
    Mockito.when(repository.searchStudentCourseStatusByStudentCourseId(1)).thenReturn(Optional.of(studentCourseStatus));
    // 例外処理の発生を検証（メッセージ含む）
    ResourceConflictException exception = assertThrows(ResourceConflictException.class, () -> sut.updateStudentCourseStatusInProgress(studentCourseId));
    assert(exception.getMessage().contains("既に完了している受講生コースです"));
  }

  @Test
  void 受講生コース申し込み状況の受講中への更新_指定されたIDの受講生コースの状態が不正の場合に例外が発生すること() {
    // 事前準備
    int studentCourseId = 1;
    StudentCourseStatus studentCourseStatus = new StudentCourseStatus(1, studentCourseId, "不正な状態") ;
    Mockito.when(repository.searchStudentCourseStatusByStudentCourseId(1)).thenReturn(Optional.of(studentCourseStatus));
    // 例外処理の発生を検証（メッセージ含む）
    ResourceConflictException exception = assertThrows(ResourceConflictException.class, () -> sut.updateStudentCourseStatusInProgress(studentCourseId));
    assert(exception.getMessage().contains("申し込み状況が不正です"));
  }

  @Test
  void 受講生コース申し込み状況の受講中への更新_指定されたIDの受講生コースが存在しない場合に例外が発生すること() {
    // 事前準備
    int studentCourseId = 1;
    StudentCourseStatus studentCourseStatus = new StudentCourseStatus(1, studentCourseId, "仮申し込み") ;
    Mockito.when(repository.searchStudentCourseStatusByStudentCourseId(studentCourseId)).thenReturn(Optional.of(studentCourseStatus));
    Mockito.when(repository.searchStudentCourseById(studentCourseId)).thenReturn(Optional.empty());
    // 例外処理の発生を検証
    assertThrows(ResourceNotFoundException.class, () -> sut.updateStudentCourseStatusInProgress(studentCourseId));
  }

  @Test
  void 受講生コース申し込み状況の完了への更新_リポジトリの処理が適切に呼び出せること() throws ResourceNotFoundException, ResourceConflictException {
    // 事前準備
    int studentCourseId = 1;
    StudentCourseStatus studentCourseStatus = new StudentCourseStatus(1, studentCourseId, "受講中") ;
    Mockito.when(repository.searchStudentCourseStatusByStudentCourseId(1)).thenReturn(Optional.of(studentCourseStatus));
    // 実行
    sut.updateStudentCourseStatusCompleted(studentCourseId);
    // 検証
    Mockito.verify(repository, Mockito.times(1)).searchStudentCourseStatusByStudentCourseId(studentCourseId);
    Mockito.verify(repository, Mockito.times(1)).updateStudentCourseStatusCompleted(studentCourseId);
  }

  @Test
  void 受講生コース申し込み状況の完了への更新_指定されたIDの受講生コースが存在しない場合に例外が発生すること() {
    // 事前準備
    int studentCourseId = 1;
    Mockito.when(repository.searchStudentCourseStatusByStudentCourseId(1)).thenReturn(Optional.empty());
    // 例外処理の発生を検証
    assertThrows(ResourceNotFoundException.class, () -> sut.updateStudentCourseStatusCompleted(studentCourseId));
  }

  @Test
  void 受講生コース申し込み状況の完了への更新_指定されたIDの受講生コースが既に完了の場合に例外が発生すること() {
    // 事前準備
    int studentCourseId = 1;
    StudentCourseStatus studentCourseStatus = new StudentCourseStatus(1, studentCourseId, "完了") ;
    Mockito.when(repository.searchStudentCourseStatusByStudentCourseId(1)).thenReturn(Optional.of(studentCourseStatus));
    // 例外処理の発生を検証（メッセージ含む）
    ResourceConflictException exception = assertThrows(ResourceConflictException.class, () -> sut.updateStudentCourseStatusCompleted(studentCourseId));
    assert(exception.getMessage().contains("既に完了している受講生コースです"));
  }

  @Test
  void 受講生コース申し込み状況の完了への更新_指定されたIDの受講生コースが仮申し込みの場合に例外が発生すること() {
    // 事前準備
    int studentCourseId = 1;
    StudentCourseStatus studentCourseStatus = new StudentCourseStatus(1, studentCourseId, "仮申し込み") ;
    Mockito.when(repository.searchStudentCourseStatusByStudentCourseId(1)).thenReturn(Optional.of(studentCourseStatus));
    // 例外処理の発生を検証（メッセージ含む）
    ResourceConflictException exception = assertThrows(ResourceConflictException.class, () -> sut.updateStudentCourseStatusCompleted(studentCourseId));
    assert(exception.getMessage().contains("仮申し込みの受講生コースは完了できません"));
  }

  @Test
  void 受講生コース申し込み状況の完了への更新_指定されたIDの受講生コースの状態が不正の場合に例外が発生すること() {
    // 事前準備
    int studentCourseId = 1;
    StudentCourseStatus studentCourseStatus = new StudentCourseStatus(1, studentCourseId, "不正な状態") ;
    Mockito.when(repository.searchStudentCourseStatusByStudentCourseId(1)).thenReturn(Optional.of(studentCourseStatus));
    // 例外処理の発生を検証（メッセージ含む）
    ResourceConflictException exception = assertThrows(ResourceConflictException.class, () -> sut.updateStudentCourseStatusCompleted(studentCourseId));
    assert(exception.getMessage().contains("申し込み状況が不正です"));
  }

  @Test
  void 受講生コース情報のID検索_リポジトリの処理が適切に呼び出せること() throws ResourceNotFoundException {
    // 事前準備
    int id = 1;
    StudentCourse studentCourse = new StudentCourse(id, 1, LocalDate.now(),
        LocalDate.now().plusWeeks(16), 1);
    Mockito.when(repository.searchStudentCourseById(id)).thenReturn(Optional.of(studentCourse));
    // 実行
    StudentCourse actual = sut.searchStudentCourseById(id);
    //
    Mockito.verify(repository, Mockito.times(1)).searchStudentCourseById(id);
  }

  @Test
  void 受講生コース情報のID検索_指定されたIDの受講生コースが存在しない場合に例外が発生すること() {
    // 事前準備
    int id = 1;
    Mockito.when(repository.searchStudentCourseById(id)).thenReturn(Optional.empty());
    // 例外処理の発生を検証
    assertThrows(ResourceNotFoundException.class, () -> sut.searchStudentCourseById(id));
  }

  @Test
  void 受講生コース申し込み状況の受講生コースIDによる検索_リポジトリの処理が適切に呼び出せること() throws ResourceNotFoundException {
    // 事前準備
    int studentCourseId = 1;
    StudentCourseStatus studentCourseStatus = new StudentCourseStatus(1, studentCourseId, "仮申し込み") ;
    Mockito.when(repository.searchStudentCourseStatusByStudentCourseId(1)).thenReturn(Optional.of(studentCourseStatus));
    // 実行
    StudentCourseStatus actual = sut.searchStudentCourseStatusByStudentCourseId(studentCourseId);
    // 検証
    Mockito.verify(repository, Mockito.times(1)).searchStudentCourseStatusByStudentCourseId(studentCourseId);
  }

  @Test
  void 受講生コース申し込み状況の受講生コースIDによる検索_指定されたIDの受講生コース申し込み状況が存在しない場合に例外が発生すること() {
    // 事前準備
    int studentCourseId = 1;
    Mockito.when(repository.searchStudentCourseStatusByStudentCourseId(1)).thenReturn(Optional.empty());
    // 例外処理の発生を検証
    assertThrows(ResourceNotFoundException.class, () -> sut.searchStudentCourseStatusByStudentCourseId(studentCourseId));
  }

}
