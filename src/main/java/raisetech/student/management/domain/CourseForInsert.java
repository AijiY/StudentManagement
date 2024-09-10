package raisetech.student.management.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "コース情報を登録するためのクラス")
@AllArgsConstructor
@Getter
public class CourseForInsert {

  @NotBlank
  private String name;

  @NotNull
  private int price;

}
