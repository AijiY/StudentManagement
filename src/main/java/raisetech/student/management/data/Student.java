package raisetech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import raisetech.student.management.domain.StudentDetailForJson;

@Schema(description = "受講生情報を保持するクラス")
@Getter
@Setter
@AllArgsConstructor // @Select用のコンストラクタ
public class Student {

  private final int id;

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

  private boolean deleted;

//  @Insert用のコンストラクタ
  public Student(StudentDetailForJson studentDetailForJson) {
    this.id = 0;
    this.name = studentDetailForJson.getName();
    this.kanaName = studentDetailForJson.getKanaName();
    this.nickname = studentDetailForJson.getNickname();
    this.email = studentDetailForJson.getEmail();
    this.livingArea = studentDetailForJson.getLivingArea();
    this.age = studentDetailForJson.getAge();
    this.gender = studentDetailForJson.getGender();
    this.remark = studentDetailForJson.getRemark();
    this.deleted = false;
  }

  // テスト用にequalsとhashCodeをオーバーライド
  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    Student student = (Student) o;

    return id == student.id &&
        age == student.age &&
        deleted == student.deleted &&
        Objects.equals(name, student.name) &&
        Objects.equals(kanaName, student.kanaName) &&
        Objects.equals(nickname, student.nickname) &&
        Objects.equals(email, student.email) &&
        Objects.equals(livingArea, student.livingArea) &&
        Objects.equals(gender, student.gender) &&
        Objects.equals(remark, student.remark);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, kanaName, nickname, email, livingArea, age, gender, remark, deleted);
  }

}
