package raisetech.student.management.service.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;

@ExtendWith(MockitoExtension.class)
class StudentConverterTest {

  @Mock
  private StudentConverter sut;
  @BeforeEach
  void setup() {
    sut = new StudentConverter();
  }

  @Test
  void 受講生詳細の変換で情報が正しくマッピングされること() {
    // 事前準備
    List<Student> students = new ArrayList<>(List.of(
        new Student(1, "AAA", "aaa", null, "aaa@example.com", null, 1, null, null, false),
        new Student(2, "BBB", "bbb", null, "bbb@example.com", null, 2, null, null, false)
    ));
    List<StudentCourse> studentCourses = new ArrayList<>(List.of(
        new StudentCourse(1, 1, null, null, 1),
        new StudentCourse(2, 1, null, null, 2),
        new StudentCourse(3, 3, null, null, 1)
    ));
    // 実行
    List<StudentDetail> actual = sut.convertStudentDetails(students, studentCourses);
    // 検証
    // actualの要素数が2であること
    assertThat(actual.size()).isEqualTo(2);
    // actualのid=1の受講生のコース情報が2件であること
    StudentDetail studentDetailStudentId1 = actual.stream()
        .filter(studentDetail -> studentDetail.getStudent().getId() == 1)
        .findFirst()
        .orElseThrow();
    assertThat(studentDetailStudentId1.getStudentCourses().size()).isEqualTo(2);
    // actualのid=2の受講生のコース情報が0件であること
    StudentDetail studentDetailStudentId2 = actual.stream()
        .filter(studentDetail -> studentDetail.getStudent().getId() == 2)
        .findFirst()
        .orElseThrow();
    assertThat(studentDetailStudentId2.getStudentCourses().size()).isEqualTo(0);
    // actualにid=3のStudentCourseが存在しないこと
    List<StudentCourse> actualStudentCourses = actual.stream()
        .flatMap(studentDetail -> studentDetail.getStudentCourses().stream())
        .toList();
    assertThat(actualStudentCourses.stream().anyMatch(studentCourse -> studentCourse.getId() == 3)).isFalse();

  }

}
