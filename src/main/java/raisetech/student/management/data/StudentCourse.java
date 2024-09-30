package raisetech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生のコース情報を保持するクラス")
@Getter
@Setter
public class StudentCourse {
  private final int id;
  private int studentId;
  private LocalDate startDate;
  private LocalDate endDueDate;
  private int courseId;
  private String courseName; // データベースには含まれない、表示用のフィールド

//  @Insert用のコンストラクタ
  public StudentCourse(int studentId, LocalDate startDate, LocalDate endDueDate, int courseId) {
    this.id = 0; // Insert時にAUTO_INCREMENTで設定される
    this.studentId = studentId;
    this.startDate = startDate;
    this.endDueDate = endDueDate;
    this.courseId = courseId;
  }

//  @Select用のcourseNameを含まないコンストラクタ
  public StudentCourse(int id, int studentId, LocalDate startDate, LocalDate endDueDate, int courseId) {
    this.id = id;
    this.studentId = studentId;
    this.startDate = startDate;
    this.endDueDate = endDueDate;
    this.courseId = courseId;
  }

  /**
   * 受講生IDとコースIDを指定して受講生のコース情報を初期化
   * 受講開始日と受講終了日は今日の日付（now）を用いて設定
   * @param studentId
   * @param courseId
   * @return
   */
  public static StudentCourse initStudentCourse(int studentId, int courseId) {
    // 仮登録用に受講開始日を1週間後に設定
    LocalDate startDate = LocalDate.now().plusWeeks(1);
    return new StudentCourse(studentId, startDate, startDate.plusWeeks(16), courseId);
  }

  // テスト用にequalsとhashCodeをオーバーライド
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    StudentCourse that = (StudentCourse) o;

    return id == that.id &&
        studentId == that.studentId &&
        courseId == that.courseId &&
        Objects.equals(startDate, that.startDate) &&
        Objects.equals(endDueDate, that.endDueDate) &&
        Objects.equals(courseName, that.courseName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, studentId, startDate, endDueDate, courseId, courseName);
  }

}
