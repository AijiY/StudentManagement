package raisetech.student.management.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor // @Select用のコンストラクタ
public class Course {
  private final int id;
  private String name;

  // @Insert用のコンストラクタ
  @JsonCreator
  public Course(@JsonProperty String name) {
    this.id = 0;
    this.name = name;
  }

}
