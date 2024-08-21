package raisetech.student.management.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

  public void registerStudent(Student student) {
    repository.insertStudent(student);
  }

  public void registerStudentCourse(StudentDetail studentDetail) {
    StudentCourse studentCourse = studentDetail.getStudentCourseList().get(0);
//    studentIdを受講生情報のidに設定
    studentCourse.setStudentId(repository.searchStudentIdByName(studentDetail.getStudent().getName()));
//    startDateを本日に設定
    studentCourse.setStartDate(LocalDate.now());
//    endDueDateを開始日から16週間後に設定
    studentCourse.setEndDueDate(studentCourse.getStartDate().plusWeeks(16));

    repository.insertStudentCourse(studentCourse);
  }
}
