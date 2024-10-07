package raisetech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生のコース情報の申し込み状況を保持するクラス")
@Getter
@Setter
@AllArgsConstructor
public class StudentCourseStatus {
  private final int id;
  private int studentCourseId;
  private String status;

  // @Insert用
  public StudentCourseStatus(int studentCourseId) {
    this.id = 0;
    this.studentCourseId = studentCourseId;
    this.status = "仮申し込み";
  }

  // テスト用にequalsとhashCodeをオーバーライド
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    StudentCourseStatus that = (StudentCourseStatus) o;
    return id == that.id &&
        studentCourseId == that.studentCourseId &&
        Objects.equals(status, that.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, studentCourseId, status);
  }
}
