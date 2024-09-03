package raisetech.student.management.data;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
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
    LocalDate now = LocalDate.now();
    return new StudentCourse(studentId, now, now.plusWeeks(16), courseId);
  }

}
