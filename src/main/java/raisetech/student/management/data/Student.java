package raisetech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import raisetech.student.management.domain.StudentForInsert;

@Schema(description = "受講生情報を保持するクラス")
@Getter
@Setter
@AllArgsConstructor // @Select用のコンストラクタ
public class Student {

  private final int id;
  private String name;
  private String kanaName;
  private String nickname;
  private String email;
  private String livingArea;
  private int age;
  private String gender;
  private String remark;
  private boolean deleted;

//  @Insert用のコンストラクタ
  public Student(StudentForInsert studentForInsert) {
    this.id = 0;
    this.name = studentForInsert.getName();
    this.kanaName = studentForInsert.getKanaName();
    this.nickname = studentForInsert.getNickname();
    this.email = studentForInsert.getEmail();
    this.livingArea = studentForInsert.getLivingArea();
    this.age = studentForInsert.getAge();
    this.gender = studentForInsert.getGender();
    this.remark = studentForInsert.getRemark();
    this.deleted = false;
  }
}
