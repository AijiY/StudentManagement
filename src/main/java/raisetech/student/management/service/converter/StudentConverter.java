package raisetech.student.management.service.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;

/**
 * 受講生と受講生コース情報を受講生詳細に変換するConverter、またはその逆
 */
@Component
public class StudentConverter {
  /**
   * 受講生情報に基づくコース情報をマッピングする
   * @param students 受講生情報
   * @param studentCourses 受講生のコース情報一覧
   * @return 受講生情報と受講生のコース情報を結合した情報
   */
  public List<StudentDetail> convertStudentDetails(List<Student> students,
      List<StudentCourse> studentCourses) {
    List<StudentDetail> studentDetails = new ArrayList<>();
    students.forEach(student -> {
      List<StudentCourse> convertStudentCourses = studentCourses.stream()
          .filter(studentCourse -> student.getId() == studentCourse.getStudentId())
          .collect(Collectors.toList());
      StudentDetail studentDetail = new StudentDetail(student, convertStudentCourses);
      studentDetails.add(studentDetail);
    });
    return studentDetails;
  }

}
