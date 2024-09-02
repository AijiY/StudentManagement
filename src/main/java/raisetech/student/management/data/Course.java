package raisetech.student.management.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Course {
  private final int id;
  private String name;

  // @Insert用のコンストラクタ
  public Course() {
    this.id = 0;
  }

}
