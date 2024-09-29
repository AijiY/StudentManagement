package raisetech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
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

  // テスト用にequalsとhashCodeをオーバーライド
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Course course = (Course) o;
    return id == course.id &&
        price == course.price &&
        name.equals(course.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, price);
  }

}
