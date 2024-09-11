package raisetech.student.management.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;

/**
 * 単一の受講生情報とその受講コース情報を保持するクラス
 */
@Schema(description = "受講生情報とその受講コース情報を保持するクラス")
@Getter
@AllArgsConstructor
public class StudentDetail {

  @Valid
  private Student student;

  private List<StudentCourse> studentCourses;
}
