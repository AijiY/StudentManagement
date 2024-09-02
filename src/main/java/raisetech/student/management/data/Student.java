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
  public Student() {
    this.id = 0;
  }

}
