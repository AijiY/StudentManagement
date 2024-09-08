package raisetech.student.management.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.data.Course;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.exception.ResourceConflictException;
import raisetech.student.management.exception.ResourceNotFoundException;
import raisetech.student.management.service.StudentService;

/**
 * 受講生の検索や登録、更新などを行うRestAPIとして実行されるController
 */
@Validated
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
    List<Student> students = service.searchStudents();
    List<StudentCourse> studentCourses = service.searchStudentCourses();

    List<StudentDetail> studentDetails = converter.convertStudentDetails(students, studentCourses);

    return studentDetails;
  }

  /**
   * 受講生検索（単一idに基づく）
   * @param id
   * @return idに対応する受講生情報
   */
  @GetMapping("/student/{id}")
  public StudentDetail getStudent(@PathVariable @Positive int id) throws ResourceNotFoundException {
    Student student = service.searchStudentById(id);
    List<StudentCourse> studentCourses = service.searchStudentCoursesByStudentId(id);
    StudentDetail studentDetail = new StudentDetail(student, studentCourses);

    return studentDetail;
  }

  /**
   * 受講生登録
   * @param student: 受講生情報
   * @param courseId: 受講生が受講するコースID
   * @return 登録した受講生詳細情報
   */
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(@RequestBody @Valid Student student, @RequestParam @Positive int courseId) {
    StudentDetail studentDetail = new StudentDetail(student, List.of(StudentCourse.initStudentCourse(student.getId(), courseId)));
    service.registerStudent(studentDetail);

    studentDetail.getStudentCourses().forEach(studentCourse -> studentCourse.setCourseName(service.searchCourseNameById(studentCourse.getCourseId())));
    return ResponseEntity.ok(studentDetail);
  }

  /**
   * 受講生更新
   * @param studentDetail
   * @return 更新した受講生詳細情報
   */
  @PutMapping("/updateStudent")
  public ResponseEntity<StudentDetail> updateStudent(@RequestBody @Valid StudentDetail studentDetail) {
    service.updateStudent(studentDetail);

    return ResponseEntity.ok(studentDetail);
  }

  /**
   * 受講生コース登録（受講生登録を伴わない個別のコース登録）
   * @param courseId
   * @param studentId
   * @return 登録した受講生コース情報
   */
  @PostMapping("/registerStudentCourse/{studentId}/{courseId}")
  public ResponseEntity<StudentCourse> registerStudentCourse(@PathVariable @Min(1) int courseId, @PathVariable @Positive int studentId) {
    StudentCourse studentCourse = StudentCourse.initStudentCourse(studentId, courseId);
    service.registerStudentCourse(studentCourse);

    studentCourse.setCourseName(service.searchCourseNameById(studentCourse.getCourseId()));
    return ResponseEntity.ok(studentCourse);
  }

  @PostMapping("/registerCourse")
  public ResponseEntity<Course> registerCourse(@RequestBody @Valid Course course) {
    service.registerCourse(course);

    return ResponseEntity.ok(course);
  }

  /**
   * 受講生論理削除
   * @param id
   * @return idと削除フラグの組み合わせ
   */
  @PatchMapping("/deleteStudent/{id}")
  public ResponseEntity<Map<String, Object>> deleteStudent(@PathVariable @Positive int id)
      throws ResourceConflictException, ResourceNotFoundException {

    service.deleteStudent(id);

    boolean deleted = service.searchStudentById(id).isDeleted();
    Map<String, Object> response = Map.of("id", id, "deleted", deleted);
    return ResponseEntity.ok(response);

  }

}
