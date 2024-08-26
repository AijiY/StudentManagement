package raisetech.student.management.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Student {
  private int id;
  private String name;
  private String kanaName;
  private String nickname;
  private String email;
  private String livingArea;
  private int age;
  private String gender;
  private String remark;
  private boolean isDeleted;

  public boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

}
