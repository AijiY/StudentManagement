package raisetech.student.management.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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
// @JsonCreatorと@JsonPropertyを使用してデシリアライズを行うコンストラクタ
  @JsonCreator
  public Student(
      @JsonProperty("name") String name,
      @JsonProperty("kanaName") String kanaName,
      @JsonProperty("nickname") String nickname,
      @JsonProperty("email") String email,
      @JsonProperty("livingArea") String livingArea,
      @JsonProperty("age") int age,
      @JsonProperty("gender") String gender,
      @JsonProperty("remark") String remark) {
    this.id = 0;
    this.name = name;
    this.kanaName = kanaName;
    this.nickname = nickname;
    this.email = email;
    this.livingArea = livingArea;
    this.age = age;
    this.gender = gender;
    this.remark = remark;
    this.deleted = false;
  }

}
