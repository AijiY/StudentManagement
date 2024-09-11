package raisetech.student.management.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "受講生情報を登録するためのクラス")
@AllArgsConstructor
@Getter
public class StudentDetailForJson {
  @NotBlank
  private String name;

  @NotBlank
  private String kanaName;

  private String nickname;

  @NotBlank
  @Email
  private String email;

  private String livingArea;

  @Positive
  private int age;

  private String gender;

  private String remark;

  @Positive
  private int courseId;
}
