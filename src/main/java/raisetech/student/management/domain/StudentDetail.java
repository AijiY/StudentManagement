package raisetech.student.management.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;

/**
 * 単一の受講生情報とその受講コース情報を保持するクラス
 */
@Getter
@AllArgsConstructor
public class StudentDetail {
  private Student student;
  private List<StudentCourse> studentCourses;
}
