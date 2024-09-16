package raisetech.student.management.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.student.management.data.Course;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
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
  public List<StudentCourse> searchStudentCourses() {
    List<StudentCourse> studentCourses = repository.searchStudentCourses();
    studentCourses.forEach(studentCourse -> {
      String courseName = repository.searchCourseNameById(studentCourse.getCourseId());
      studentCourse.setCourseName(courseName);
    });
    return studentCourses;
  }

  /**
   * 受講生IDを指定して受講生情報を検索
   * @param id
   * @return
   */
  public Student searchStudentById(int id) throws ResourceNotFoundException {
    Student student = repository.searchStudentById(id);
    if (student == null) {
      throw new ResourceNotFoundException("指定されたIDの受講生は存在しません");
    }
    return student;
  }

  /**
   * 受講生IDを指定して受講生のコース情報を検索
   * コースidからコース名も取得して設定
   * @param studentId
   * @return
   */
  public List<StudentCourse> searchStudentCoursesByStudentId(int studentId) {
    List<StudentCourse> studentCourses = repository.searchStudentCoursesByStudentId(studentId);
    studentCourses.forEach(studentCourse -> {
      String courseName = repository.searchCourseNameById(studentCourse.getCourseId());
      studentCourse.setCourseName(courseName);
    });
    return studentCourses;
  }

  /**
   * 受講生詳細情報を全件検索
   * 受講生情報と受講生のコース情報を結合した情報を返却
   * @return 受講生詳細情報一覧
   */
  public List<StudentDetail> searchStudentDetails() {
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
  public String searchCourseNameById(int id) {
    return repository.searchCourseNameById(id);
  }

  /**
   * 受講生情報を新規登録
   * 初期コース情報も同時に登録する
   * @param studentDetail（受講生情報＋コース情報）
   */
  @Transactional
  public void registerStudent(StudentDetail studentDetail) {
//    ①受講生情報を登録
    Student student = studentDetail.getStudent();
    repository.insertStudent(student);

//    ②コース情報を登録
    StudentCourse studentCourse = studentDetail.getStudentCourses().get(0);
    studentCourse.setStudentId(student.getId());
    repository.insertStudentCourse(studentCourse);
  }

  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
//    ①受講生情報を更新
    Student student = studentDetail.getStudent();
    repository.updateStudent(student);
  }

  @Transactional
  public void registerStudentCourse(StudentCourse studentCourse) {
    repository.insertStudentCourse(studentCourse);
  }

  @Transactional
  public void registerCourse(Course course) {
    repository.insertCourse(course);
  }

  @Transactional
  public void deleteStudent(int id) throws ResourceConflictException, ResourceNotFoundException {
    Student student = repository.searchStudentById(id);
    if (student == null) {
      throw new ResourceNotFoundException("指定されたIDの受講生は存在しません");
    }
    if (student.isDeleted()) {
      throw new ResourceConflictException("既に削除されている受講生です");
    }

    repository.deleteStudent(id);
  }
}
