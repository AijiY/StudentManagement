package raisetech.student.management.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.repository.StudentRepository;

/**
 * 受講生情報を扱うサービス
 * 検索や登録、更新などを行う
 */
@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList() {
    return repository.searchStudent();
  }

  public List<StudentCourse> searchStudentCourseList() {
    return repository.searchStudentCourse();
  }

  public Student searchStudentById(int id) {
    return repository.searchStudentById(id);
  }

  public List<StudentCourse> searchStudentCoursesByStudentId(int studentId) {
    return repository.searchStudentCoursesByStudentId(studentId);
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
//    isDeletedをfalseに設定
    student.setDeleted(false);
    repository.insertStudent(student);

//    ②コース情報を登録
    StudentCourse studentCourse = studentDetail.getStudentCourses().get(0);
//    studentIdを登録した受講生のidに設定
    studentCourse.setStudentId(studentDetail.getStudent().getId());
//    startDateを本日に設定
    studentCourse.setStartDate(LocalDate.now());
//    endDueDateを開始日から16週間後に設定
    studentCourse.setEndDueDate(studentCourse.getStartDate().plusWeeks(16));

    repository.insertStudentCourse(studentCourse);
  }

  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
//    ①受講生情報を更新
    Student student = studentDetail.getStudent();
    repository.updateStudent(student);
  }
}
