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

//  以下、データベースには含まれない
  private String courseName;

//  @Insert用のコンストラクタ
  public StudentCourse() {
    this.id = 0;
  }

}
