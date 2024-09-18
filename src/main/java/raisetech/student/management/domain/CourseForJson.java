package raisetech.student.management.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "コース情報を登録するためのクラス")
@AllArgsConstructor
@Getter
public class CourseForJson {

  @NotBlank
  private String name;

  @NotNull
  @Positive
  private int price;

}
