package raisetech.student.management.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList() {
//    検索処理
    List<Student> studentList = repository.searchStudent();
//    年齢が30代の受講生を抽出
    List<Student> studentListIn30s = studentList.stream()
        .filter(student -> student.getAge() >= 30 && student.getAge() < 40)
        .toList();

//    抽出したリストをコントローラーに返す
    return studentListIn30s;
  }

  public List<StudentCourse> searchStudentCourseList() {
//    検索処理
    List<StudentCourse> studentCourseList = repository.searchStudentCourse();
//    コース情報が「Javaコース」の受講生を抽出
    List<StudentCourse> studentCourseListInJava = studentCourseList.stream()
        .filter(studentCourse -> studentCourse.getCourseName().equals("Javaコース"))
        .toList();
//    抽出したリストをコントローラーに返す
    return studentCourseListInJava;
  }
}
