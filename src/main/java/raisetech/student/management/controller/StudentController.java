package raisetech.student.management.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.service.StudentService;

/**
 * 受講生の検索や登録、更新などを行うRestAPIとして実行されるController
 */
@RestController
public class StudentController {

  private StudentService service;
  private StudentConverter converter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  /**
   * 受講生一覧検索
   * 全件検索を実施するので、条件指定なし
   * @return 受講生一覧
   */
  @GetMapping("/students")
  public List<StudentDetail> getStudents() {
    List<Student> students = service.searchStudentList();
    List<StudentCourse> studentCourses = service.searchStudentCourseList();

    List<StudentDetail> studentDetails = converter.convertStudentDetails(students, studentCourses);

    return studentDetails;
  }

  /**
   * 受講生検索（単一idに基づく）
   * @param id
   * @return idに対応する受講生情報
   */
  @GetMapping("/student/{id}")
  public StudentDetail getStudent(@PathVariable int id) {
    Student student = service.searchStudentById(id);
    List<StudentCourse> studentCourses = service.searchStudentCoursesByStudentId(id);
    StudentDetail studentDetail = new StudentDetail(student, studentCourses);

    return studentDetail;
  }

  /**
   * 受講生登録
   * @param studentDetail
   * @return 登録した受講生情報
   */
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(@RequestBody StudentDetail studentDetail) {
    service.registerStudent(studentDetail);

    return ResponseEntity.ok(studentDetail);
  }

  /**
   * 受講生更新
   * @param studentDetail
   * @return 更新した受講生情報
   */
  @PostMapping("/updateStudent")
  public ResponseEntity<StudentDetail> updateStudent(@RequestBody StudentDetail studentDetail) {
    service.updateStudent(studentDetail);

    return ResponseEntity.ok(studentDetail);
  }

}
