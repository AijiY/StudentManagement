package raisetech.student.management.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudentCourseForJson {
  @Positive
  private int studentId;

  @Positive
  private int courseId;

}
