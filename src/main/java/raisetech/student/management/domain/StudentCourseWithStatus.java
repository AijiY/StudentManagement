package raisetech.student.management.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Getter;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.StudentCourseStatus;

@Schema(description = "受講生のコース情報に申し込み状況を追加したクラス")
@Getter
public class StudentCourseWithStatus {
  private final int id;
  private int studentId;
  private LocalDate startDate;
  private LocalDate endDueDate;
  private int courseId;
  private String status; // 申し込み状況

  public StudentCourseWithStatus(StudentCourse studentCourse, StudentCourseStatus studentCourseStatus) {
    this.id = studentCourse.getId();
    this.studentId = studentCourse.getStudentId();
    this.startDate = studentCourse.getStartDate();
    this.endDueDate = studentCourse.getEndDueDate();
    this.courseId = studentCourse.getCourseId();
    this.status = studentCourseStatus.getStatus();
  }
}
