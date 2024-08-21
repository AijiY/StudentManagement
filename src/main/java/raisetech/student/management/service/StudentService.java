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
//    idを自動採番
    student.setId(repository.searchMaxStudentId() + 1);
    repository.insertStudent(student);
  }

  public void registerStudentCourse(StudentDetail studentDetail) {
    StudentCourse studentCourse = studentDetail.getStudentCourseList().get(0);
//    idを自動採番
    studentCourse.setId(repository.searchMaxCourseId() + 1);
//    studentIdを受講生情報のidに設定
    studentCourse.setStudentId(studentDetail.getStudent().getId());
//    startDateを本日に設定
    studentCourse.setStartDate(LocalDate.now());
//    endDueDateを開始日から16週間後に設定
    studentCourse.setEndDueDate(studentCourse.getStartDate().plusWeeks(16));

    repository.insertStudentCourse(studentCourse);
  }
}
