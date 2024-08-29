package raisetech.student.management.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.service.StudentService;

@RestController
public class StudentController {

  private StudentService service;
  private StudentConverter converter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  @GetMapping("/students")
  public List<StudentDetail> getStudents() {
    List<Student> students = service.searchStudentList();
    List<StudentCourse> studentCourses = service.searchStudentCourseList();

    List<StudentDetail> studentDetails = converter.convertStudentDetails(students, studentCourses);

    return studentDetails;
  }

//  Restへ未移行
  @GetMapping("/newStudent")
  public String newStudent(Model model) {
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudentCourseList(new ArrayList<>());
    studentDetail.getStudentCourseList().add(new StudentCourse());

    model.addAttribute("studentDetail", studentDetail);
    return "registerStudent";
  }

  @GetMapping("/student/{id}")
  public StudentDetail getStudent(@PathVariable int id) {
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(service.searchStudentById(id));
    studentDetail.setStudentCourseList(service.searchStudentCoursesByStudentId(id));

    return studentDetail;
  }

//  Restへ未移行
  @PostMapping("/registerStudent")
  public String registerStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    if (result.hasErrors()) {
      return "registerStudent";
    }

//    新規の受講生情報を登録する処理を実施する
    service.registerStudent(studentDetail);

    return "redirect:/students";
  }

  @PostMapping("/updateStudent")
  public ResponseEntity<StudentDetail> updateStudent(@RequestBody StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok(studentDetail);
  }

}
