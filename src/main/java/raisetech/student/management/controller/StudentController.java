package raisetech.student.management.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.service.StudentService;

@Controller
public class StudentController {

  private StudentService service;
  private StudentConverter converter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  @GetMapping("/students")
  public String getStudentList(Model model) {
    List<Student> students = service.searchStudentList();
    List<StudentCourse> studentCourses = service.searchStudentCourseList();

    List<StudentDetail> studentDetails = converter.convertStudentDetails(students, studentCourses);
    model.addAttribute("studentDetails", studentDetails);

    return "students"; // ここでThymeleafテンプレートを返す
  }

  @GetMapping("/newStudent")
  public String newStudent(Model model) {
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudentCourseList(new ArrayList<>());
    studentDetail.getStudentCourseList().add(new StudentCourse());

    model.addAttribute("studentDetail", studentDetail);
    return "registerStudent";
  }

  @GetMapping("/student")
  public String updateStudent(@RequestParam int id, Model model) {
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(service.searchStudentById(id));
    studentDetail.setStudentCourseList(service.searchStudentCoursesByStudentId(id));

    model.addAttribute("studentDetail", studentDetail);
    return "updateStudent";
  }

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
  public String updateStudent(@ModelAttribute StudentDetail studentDetail, @RequestParam int id, BindingResult result) {
    if (result.hasErrors()) {
      return "updateStudent";
    }

    studentDetail.getStudent().setId(id);
    service.updateStudent(studentDetail);

    return "redirect:/students";
  }
}
