package raisetech.student.management.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "受講生に対して新規の受講生コースを登録するためのクラス")
@Getter
@AllArgsConstructor
public class StudentCourseForJson {
  @Positive
  private int studentId;

  @Positive
  private int courseId;

}
