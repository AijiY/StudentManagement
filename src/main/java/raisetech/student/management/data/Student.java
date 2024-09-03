package raisetech.student.management.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
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
  public Student(String name, String kanaName, String nickname, String email,
      String livingArea, int age, String gender, String remark) {
    this.id = 0; // Insert時にAUTO_INCREMENTで設定される
    this.name = name;
    this.kanaName = kanaName;
    this.nickname = nickname;
    this.email = email;
    this.livingArea = livingArea;
    this.age = age;
    this.gender = gender;
    this.remark = remark;
    this.deleted = false; // デフォルトはfalse
  }

  // デフォルトコンストラクタ（@RequestBodyで必要）
  public Student() {
    this.id = 0; // デフォルト値
    this.deleted = false; // デフォルト値
  }

}
