package raisetech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import raisetech.student.management.domain.CourseForJson;

@Schema(description = "コース情報を保持するクラス")
@Getter
@Setter
@AllArgsConstructor // @Select用のコンストラクタ
public class Course {

  private final int id;
  private String name;
  private int price;

  // @Insert用のコンストラクタ
  public Course(CourseForJson courseForJson) {
    this.id = 0;
    this.name = courseForJson.getName();
    this.price = courseForJson.getPrice();
  }

}
