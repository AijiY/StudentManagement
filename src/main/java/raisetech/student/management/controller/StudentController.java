package raisetech.student.management.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.student.management.data.Course;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.CourseForJson;
import raisetech.student.management.domain.ResponseForDelete;
import raisetech.student.management.domain.StudentCourseForJson;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.domain.StudentDetailForJson;
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

  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  /**
   * 受講生一覧検索
   * 全件検索を実施するので、条件指定なし
   * @return 受講生一覧
   */
  @Operation(summary = "受講生一覧検索", description = "全ての受講生情報を取得します")
  @GetMapping("/students")
  public List<StudentDetail> getStudents() {

    List<StudentDetail> studentDetails = service.searchStudentDetails();

    return studentDetails;
  }

  /**
   * 受講生検索（単一idに基づく）
   * @param id
   * @return idに対応する受講生情報
   */
  @Operation(summary = "受講生検索", description = "指定されたIDの受講生情報（コース情報含む）を取得します")
  @GetMapping("/students/{id}")
  public StudentDetail getStudent(@PathVariable @Positive int id) throws ResourceNotFoundException {

    StudentDetail studentDetail = service.searchStudentDetailById(id);

    return studentDetail;
  }

  /**
   * 受講生登録
   * @param studentDetailForJson: 受講生登録情報（受講生情報＋初期コースid）
   * @return 登録した受講生詳細情報
   */
  @Operation(summary = "受講生登録", description = "新規の受講生情報（初期コース情報含む）を登録します")
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(@RequestBody @Valid StudentDetailForJson studentDetailForJson) {
    Student student = new Student(studentDetailForJson);
    StudentCourse studentCourse = StudentCourse.initStudentCourse(0, studentDetailForJson.getCourseId()); // この時点でidは不明なので0
    StudentDetail studentDetail = new StudentDetail(student, List.of(studentCourse));
    service.registerStudent(studentDetail);

    studentDetail.getStudentCourses().forEach(sc -> sc.setCourseName(service.searchCourseNameById(sc.getCourseId())));
    return ResponseEntity.ok(studentDetail);
  }

  /**
   * 受講生更新
   * @param studentDetail
   * @return 更新した受講生詳細情報
   */
  @Operation(summary = "受講生更新", description = "指定された受講生情報を更新します")
  @PutMapping("/updateStudent")
  public ResponseEntity<StudentDetail> updateStudent(@RequestBody @Valid StudentDetail studentDetail) {
    service.updateStudent(studentDetail);

    return ResponseEntity.ok(studentDetail);
  }

  /**
   * 受講生コース登録（受講生登録を伴わない個別のコース登録）
   * @param studentCourseForJson
   * @return 登録した受講生コース情報
   */
  @Operation(summary = "受講生コース登録", description = "指定された受講生に対して指定されたコースを登録します")
  @PostMapping("/registerStudentCourse")
  public ResponseEntity<StudentCourse> registerStudentCourse
    (@RequestBody @Valid StudentCourseForJson studentCourseForJson) {
    StudentCourse studentCourse = StudentCourse.initStudentCourse(
        studentCourseForJson.getStudentId(), studentCourseForJson.getCourseId());
    service.registerStudentCourse(studentCourse);

    studentCourse.setCourseName(service.searchCourseNameById(studentCourse.getCourseId()));
    return ResponseEntity.ok(studentCourse);
  }

  /**
   * コース登録
   * @param courseForJson
   * @return 登録したコース情報
   */
  @Operation(summary = "コース登録", description = "新規のコース情報を登録します")
  @PostMapping("/registerCourse")
  public ResponseEntity<Course> registerCourse(@RequestBody @Valid CourseForJson courseForJson) {
    Course course = new Course(courseForJson);
    service.registerCourse(course);

    return ResponseEntity.ok(course);
  }

  /**
   * 受講生論理削除
   * @param id
   * @return idと削除フラグの組み合わせ
   */
  @Operation(summary = "受講生論理削除", description = "指定されたIDの受講生情報を論理削除します")
  @PatchMapping("/deleteStudent/{id}")
  public ResponseEntity<ResponseForDelete> deleteStudent(@PathVariable @Positive int id)
      throws ResourceConflictException, ResourceNotFoundException {

    service.deleteStudent(id);

    boolean deleted = service.searchStudentById(id).isDeleted();
    ResponseForDelete response = new ResponseForDelete(id, deleted);
    return ResponseEntity.ok(response);
  }

}
