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

  @Transactional
  public void registerStudent(StudentDetail studentDetail) {
//    ①受講生情報を登録
    Student student = studentDetail.getStudent();
//    isDeletedをfalseに設定
    student.setIsDeleted(false);
    repository.insertStudent(student);

//    ②コース情報を登録
    StudentCourse studentCourse = studentDetail.getStudentCourseList().get(0);
//    studentIdを最新登録のものに設定
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
