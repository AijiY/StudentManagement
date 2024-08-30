package raisetech.student.management.data;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentCourse {
  private int id;
  private int studentId;
  private LocalDate startDate;
  private LocalDate endDueDate;
  private int courseId;

//  以下、データベースには含まれない
  private String courseName;
}
