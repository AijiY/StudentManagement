package raisetech.student.management.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.student.management.data.Course;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.StudentCourseStatus;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.exception.ResourceConflictException;
import raisetech.student.management.exception.ResourceNotFoundException;
import raisetech.student.management.repository.StudentRepository;
import raisetech.student.management.service.converter.StudentConverter;

/**
 * 受講生情報を扱うサービス
 * 検索や登録、更新などを行う
 */
@Service
@Transactional
public class StudentService {

  private StudentRepository repository;
  private StudentConverter converter;

  @Autowired
  public StudentService(StudentRepository repository, StudentConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  /**
   * 受講生情報を全件検索
   * @return
   */
  public List<Student> searchStudents() {
    return repository.searchStudents();
  }

  /**
   * 受講生のコース情報を全件検索
   * コースidからコース名も取得して設定
   * @return
   */
  public List<StudentCourse> searchStudentCourses() throws ResourceNotFoundException {
    List<StudentCourse> studentCourses = repository.searchStudentCourses();
    for (StudentCourse studentCourse : studentCourses) {
      String courseName = searchCourseNameById(studentCourse.getCourseId());
      studentCourse.setCourseName(courseName);
    }
    return studentCourses;
  }

  /**
   * 受講生IDを指定して受講生情報を検索
   * @param id
   * @return
   */
  public Student searchStudentById(int id) throws ResourceNotFoundException {
    return repository.searchStudentById(id)
        .orElseThrow(() -> new ResourceNotFoundException("指定されたIDの受講生は存在しません"));
  }

  /**
   * 受講生IDを指定して受講生のコース情報を検索
   * コースidからコース名も取得して設定
   * @param studentId
   * @return
   */
  public List<StudentCourse> searchStudentCoursesByStudentId(int studentId) throws ResourceNotFoundException {
    List<StudentCourse> studentCourses = repository.searchStudentCoursesByStudentId(studentId);
    for (StudentCourse studentCourse : studentCourses) {
      String courseName = searchCourseNameById(studentCourse.getCourseId());
      studentCourse.setCourseName(courseName);
    }
    return studentCourses;
  }

  /**
   * 受講生詳細情報を全件検索
   * 受講生情報と受講生のコース情報を結合した情報を返却
   * @return 受講生詳細情報一覧
   */
  public List<StudentDetail> searchStudentDetails() throws ResourceNotFoundException {
    List<Student> students = searchStudents();
    List<StudentCourse> studentCourses = searchStudentCourses();
    return converter.convertStudentDetails(students, studentCourses);
  }

  /**
   * 受講生IDを指定して受講生詳細情報を検索
   * 受講生情報と受講生のコース情報を結合した情報を返却
   * @param id
   * @return idに対応する受講生詳細情報
   */
  public StudentDetail searchStudentDetailById(int id) throws ResourceNotFoundException {
    Student student = searchStudentById(id);
    List<StudentCourse> studentCourses = searchStudentCoursesByStudentId(id);
    return new StudentDetail(student, studentCourses);
  }

  /**
   * コース情報を全件検索
   */
  public List<Course> searchCourses() {
    return repository.searchCourses();
  }

  /**
   * コースIDを指定してコース名を検索
   * @param id
   * @return
   */
  public String searchCourseNameById(int id) throws ResourceNotFoundException {
    return repository.searchCourseNameById(id)
        .orElseThrow(() -> new ResourceNotFoundException("指定されたIDのコースは存在しません"));
  }

  /**
   * 受講生情報を新規登録
   * 初期コース情報も同時に登録する
   * @param studentDetail（受講生情報＋コース情報）
   */
  @Transactional(rollbackFor = ResourceNotFoundException.class)
  public void registerStudent(StudentDetail studentDetail) throws ResourceNotFoundException {
//    ①受講生情報を登録
    Student student = studentDetail.getStudent();
    repository.insertStudent(student);

//    ②コース情報を登録
    StudentCourse studentCourse = studentDetail.getStudentCourses().get(0);
    studentCourse.setStudentId(student.getId());
    registerStudentCourse(studentCourse);
  }

  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
//    ①受講生情報を更新
    Student student = studentDetail.getStudent();
    repository.updateStudent(student);
  }

  @Transactional
  public void registerStudentCourse(StudentCourse studentCourse) throws ResourceNotFoundException {
    // courseIdが存在するかを確認
    searchCourseNameById(studentCourse.getCourseId());
    // studentIdが存在するかを確認
    searchStudentById(studentCourse.getStudentId());
    // 存在する場合は登録（同時に申し込み状況を新規登録）
    repository.insertStudentCourse(studentCourse);
    repository.insertStudentCourseStatus(new StudentCourseStatus(studentCourse.getId()));
  }

  @Transactional
  public void registerCourse(Course course) {
    repository.insertCourse(course);
  }

  @Transactional
  public void deleteStudent(int id) throws ResourceConflictException, ResourceNotFoundException {
    Student student = repository.searchStudentById(id)
        .orElseThrow(() -> new ResourceNotFoundException("指定されたIDの受講生は存在しません"));
    if (student.isDeleted()) {
      throw new ResourceConflictException("既に削除されている受講生です");
    }

    repository.deleteStudent(id);
  }

  @Transactional(rollbackFor = ResourceNotFoundException.class)
  public void updateStudentCourseStatusInProgress(int studentCourseId) throws ResourceNotFoundException, ResourceConflictException {
    // 存在する受講生コースIDかを確認
    StudentCourseStatus studentCourseStatus = repository.searchStudentCourseStatusByStudentCourseId(studentCourseId)
        .orElseThrow(() -> new ResourceNotFoundException("指定されたIDの受講生コース申し込み状況は存在しません"));
    // 現在の申し込み状況が仮申し込みでない場合はResourceConflictExceptionをスロー
    if (studentCourseStatus.getStatus().equals("受講中")) {
      throw new ResourceConflictException("既に受講中の受講生コースです");
    } else if (studentCourseStatus.getStatus().equals("完了")) {
      throw new ResourceConflictException("既に完了している受講生コースです");
    } else if (!studentCourseStatus.getStatus().equals("仮申し込み")) {
      throw new ResourceConflictException("申し込み状況が不正です");
    }
    // 問題なければ受講中に更新
    repository.updateStudentCourseStatusInProgress(studentCourseId);
    // startDateとendDueDateを設定
    updateStudentCourseDateInProgress(studentCourseId);
  }

  private void updateStudentCourseDateInProgress(int studentCourseId) throws ResourceNotFoundException {
    StudentCourse studentCourse = repository.searchStudentCourseById(studentCourseId)
        .orElseThrow(() -> new ResourceNotFoundException("指定されたIDの受講生コース情報は存在しません"));
    LocalDate now = LocalDate.now();
    studentCourse.setStartDate(now);
    studentCourse.setEndDueDate(now.plusWeeks(16));
    repository.updateStudentCourse(studentCourse);
  }

  @Transactional
  public void updateStudentCourseStatusCompleted(int studentCourseId) throws ResourceNotFoundException, ResourceConflictException {
    // 存在する受講生コースIDかを確認
    StudentCourseStatus studentCourseStatus = repository.searchStudentCourseStatusByStudentCourseId(studentCourseId)
        .orElseThrow(() -> new ResourceNotFoundException("指定されたIDの受講生コース情報は存在しません"));
    // 現在の申し込み状況が受講中でない場合はResourceConflictExceptionをスロー
    if (studentCourseStatus.getStatus() == "完了") {
      throw new ResourceConflictException("既に完了している受講生コースです");
    } else if (studentCourseStatus.getStatus() == "仮申し込み") {
      throw new ResourceConflictException("仮申し込みの受講生コースは完了できません");
    } else if (!studentCourseStatus.getStatus().equals("受講中")) {
      throw new ResourceConflictException("申し込み状況が不正です");
    }
    // 問題なければ完了に更新
    repository.updateStudentCourseStatusCompleted(studentCourseId);
  }

  public StudentCourse searchStudentCourseById(int id) throws ResourceNotFoundException {
    return repository.searchStudentCourseById(id)
        .orElseThrow(() -> new ResourceNotFoundException("指定されたIDの受講生コース情報は存在しません"));
  }

  public StudentCourseStatus searchStudentCourseStatusByStudentCourseId(int studentCourseId) throws ResourceNotFoundException {
    return repository.searchStudentCourseStatusByStudentCourseId(studentCourseId)
        .orElseThrow(() -> new ResourceNotFoundException("指定されたIDの受講生コース申し込み状況は存在しません"));
  }
}
