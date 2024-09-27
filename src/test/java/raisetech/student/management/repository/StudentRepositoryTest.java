package raisetech.student.management.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.student.management.data.Course;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.CourseForJson;
import raisetech.student.management.domain.StudentDetailForJson;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  @Test
  void 受講生情報の全件検索が実施できること_deletedがfalseの受講生情報が適切であること() {
    List<Student> actual = sut.searchStudents();

    List<Student> expected = List.of(
        new Student(1, "山田太郎", "やまだたろう", "たろう", "taro.yamada@example.com", "東京都新宿区", 20, "男性", "", false),
        new Student(2, "鈴木花子", "すずきはなこ", "はな", "hanako.suzuki@example.com", "大阪府大阪市", 22, "女性", "", false),
        new Student(3, "佐藤健", "さとうたける", "たけ", "take.sato@example.com", "愛知県名古屋市", 21, "男性", "", false),
        new Student(4, "高橋美咲", "たかはしみさき", "みさき", "misaki.takahashi@example.com", "福岡県福岡市", 23, "女性", "", false)
    );

    assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
  }

  @Test
  void 受講生のコース情報の全件検索が実施できること_情報が適切であること() {
    List<StudentCourse> actual = sut.searchStudentCourses();

    List<StudentCourse> expected = List.of(
        new StudentCourse(1, 1, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 4, 25), 1),
        new StudentCourse(2, 1, LocalDate.of(2024, 4,26), LocalDate.of(2024,8,25), 2),
        new StudentCourse(3, 2, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 4, 25), 3),
        new StudentCourse(4, 3, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 4, 25), 1),
        new StudentCourse(5, 4, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 4, 25), 2),
        new StudentCourse(6, 5, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 4, 25), 4)
    );

    assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
  }

  @Test
  void 受講生IDを指定して受講生情報を検索が実施できること_指定したIDの受講生情報が取得できること() {
    // IDが1の受講生の情報を取得
    Student actual = sut.searchStudentById(1);
    // expectedにIDが1の受講生情報を設定
    Student expected = new Student(1, "山田太郎", "やまだたろう", "たろう", "taro.yamada@example.com", "東京都新宿区", 20, "男性", "", false);
    // 2つの情報が一致していることを確認
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void 受講生IDを指定して受講生のコース情報を検索が実施できること_指定したIDの受講生のコース情報が取得できること() {
    List<StudentCourse> actual = sut.searchStudentCoursesByStudentId(1);
    List<StudentCourse> expected = List.of(
        new StudentCourse(1, 1, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 4, 25), 1),
        new StudentCourse(2, 1, LocalDate.of(2024, 4,26), LocalDate.of(2024,8,25), 2)
    );
    assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
  }

  @Test
  void コース情報の全件検索が実施できること_情報が適切であること() {
    List<Course> actual = sut.searchCourses();
    List<Course> expected = List.of(
        new Course(1, "Javaコース", 200000),
        new Course(2, "PHPコース", 180000),
        new Course(3, "Rubyコース", 150000),
        new Course(4, "Pythonコース", 250000)
    );
    assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
  }

  @Test
  void コースIDを指定してコース名を検索が実施できること_指定したIDのコース名が取得できること() {
    String actual = sut.searchCourseNameById(1);
    assertThat(actual).isEqualTo("Javaコース");
  }

  @Test
  void 受講生情報の新規登録が実施できること_登録前後でstudents件数が1件増えていること() {
    StudentDetailForJson studentDetailForJson = new StudentDetailForJson(
        "テスト太郎", "テストタロウ", "テスト", "test@example.com",
        "テスト県テスト市", 20, "男性", null, 1
    );
    Student student = new Student(studentDetailForJson);
    int countPresentStudents = sut.searchStudents().size();
    sut.insertStudent(student);
    List<Student> actual = sut.searchStudents();
    assertThat(actual.size()).isEqualTo(countPresentStudents + 1);
  }

  @Test
  void 受講生のコース情報の新規登録が実施できること_登録前後でstudent_courses件数が1件増えていること() {
    StudentCourse studentCourse = StudentCourse.initStudentCourse(1, 3);
    int countPresentStudentCourses = sut.searchStudentCourses().size();
    sut.insertStudentCourse(studentCourse);
    List<StudentCourse> actual = sut.searchStudentCourses();
    assertThat(actual.size()).isEqualTo(countPresentStudentCourses + 1);
  }

  @Test
  void コース情報の新規登録が実施できること_登録前後でcourses件数が1件増えていること() {
    CourseForJson courseForJson = new CourseForJson("テストコース", 100000);
    Course course = new Course(courseForJson);
    int countPresentCourses = sut.searchCourses().size();
    sut.insertCourse(course);
    List<Course> actual = sut.searchCourses();
    assertThat(actual.size()).isEqualTo(countPresentCourses + 1);
  }

  @Test
  void 受講生情報の更新が実施できること_更新に使用した情報と更新後の情報が一致していること() {
    Student student = sut.searchStudentById(1);
    // 元の情報を保存
    Student original = student;
    // nicknameとgenderを変更
    student.setNickname("テストニックネーム");
    student.setGender("不明");
    // 更新
    sut.updateStudent(student);
    Student actual = sut.searchStudentById(1);
    // nicknameとgenderが変更されていることを確認
    assertThat(actual.getNickname()).isEqualTo("テストニックネーム");
    assertThat(actual.getGender()).isEqualTo("不明");
    // 他の情報は変更されていないことを確認
    assertThat(actual)
        .usingRecursiveComparison()
        .ignoringFields("nickname", "gender")
        .isEqualTo(original);
  }

  @Test
  void 受講生情報の論理削除が実施できること_削除フラグがtrueになっていること() {
    sut.deleteStudent(1);
    Student actual = sut.searchStudentById(1);
    assertThat(actual.isDeleted()).isTrue();
  }
}
