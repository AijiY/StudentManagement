package raisetech.student.management.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import raisetech.student.management.data.Course;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.exception.ResourceConflictException;
import raisetech.student.management.exception.ResourceNotFoundException;
import raisetech.student.management.service.StudentService;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private StudentService service;

  @Test
  void 受講生一覧検索ができること() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/students"))
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudentDetails();
  }

  @Test
  void 受講生ID検索ができること() throws Exception {
    int id = 1;
    mockMvc.perform(MockMvcRequestBuilders.get("/students/" + id))
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudentDetailById(id);
  }

  @Test
  void 受講生ID検索でパスのIDが不正な場合にConstraintViolationExceptionが発生すること() throws Exception {
    int id = 0;
    mockMvc.perform(MockMvcRequestBuilders.get("/students/" + id))
        .andExpect(status().isBadRequest())
        .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConstraintViolationException));
  }

  @Test
  void 受講生ID検索でIDに対応する受講生が存在しない場合にResourceNotFoundExceptionが発生すること() throws Exception {
    int id = 1;
    when(service.searchStudentDetailById(id)).thenThrow(new ResourceNotFoundException("指定されたIDの受講生は存在しません"));
    mockMvc.perform(MockMvcRequestBuilders.get("/students/" + id))
        .andExpect(status().isNotFound())
        .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException));
  }

  @Test
  void 受講生登録ができること() throws Exception {
    // RequestBodyを作成
    String requestBody = """
        {
            "name": "name",
            "kanaName": "kanaName",
            "nickname": null,
            "email": "aaa@example.com",
            "livingArea": null,
            "age": 1,
            "gender": null,
            "remark": null,
            "courseId": 1
        }
        """;

    mockMvc.perform(MockMvcRequestBuilders.post("/registerStudent")
        .contentType("application/json")
        .content(requestBody))
        .andExpect(status().isOk());

    verify(service, times(1)).registerStudent(any(StudentDetail.class));
    verify(service, times(1)).searchCourseNameById(any(int.class));
  }

  @ParameterizedTest
  @CsvSource({
      // フィールドに不足がある場合（@Blank対象）
      "'', '', null, '', null, 1, null, null, 1, 3, 'name, kanaName, email', "
          + "'must not be blank, must not be blank, must not be blank'",
      // フィールドに不正な値がある場合（@Email、@Positive対象）
      "'name', 'kanaName', null, 'aaa', null, 0, null, null, 0, 3, 'email, age, courseId', "
          + "'must be a well-formed email address, must be greater than 0, must be greater than 0'"
  })
  void 受講生登録のバリデーションテスト(String name, String kanaName, String nickname, String email, String livingArea,
      int age, String gender, String remark, int courseId, int expectedErrorCount,
      String expectedErrorFields, String expectedErrorMessages) throws Exception {

    // JSON形式のRequestBodyを作成
    String requestBody = String.format("""
        {
            "name": "%s",
            "kanaName": "%s",
            "nickname": %s,
            "email": "%s",
            "livingArea": %s,
            "age": %d,
            "gender": %s,
            "remark": %s,
            "courseId": %d
        }
        """,
        name,
        kanaName,
        nickname,
        email,
        livingArea,
        age,
        gender,
        remark,
        courseId
    );

    mockMvc.perform(MockMvcRequestBuilders.post("/registerStudent")
            .contentType("application/json")
            .content(requestBody))
        .andExpect(status().isBadRequest()) // バリデーションエラーを期待
        .andExpect(result -> {
          // 例外を取得
          MethodArgumentNotValidException ex = (MethodArgumentNotValidException) result.getResolvedException();

          // BindingResultを取得し、すべてのエラーを確認
          BindingResult bindingResult = ex.getBindingResult();
          List<FieldError> fieldErrors = bindingResult.getFieldErrors();

          // バリデーションエラーが期待通りの数か確認
          assertThat(fieldErrors.size()).isEqualTo(expectedErrorCount);

          // フィールド名とエラーメッセージの配列を取得
          String[] expectedFields = expectedErrorFields.split(",");
          String[] expectedMessages = expectedErrorMessages.split(",");

          // 各フィールド名とメッセージをトリム（余分なスペースを削除）
          expectedFields = Arrays.stream(expectedFields).map(String::trim).toArray(String[]::new);
          expectedMessages = Arrays.stream(expectedMessages).map(String::trim).toArray(String[]::new);

          // 各フィールドのエラーメッセージを確認
          assertThat(fieldErrors)
              .extracting(FieldError::getField)
              .containsExactlyInAnyOrder(expectedFields);

          // 各フィールドのエラーメッセージ内容も確認
          assertThat(fieldErrors)
              .extracting(FieldError::getDefaultMessage)
              .containsExactlyInAnyOrder(expectedMessages);
        });
  }

  @Test
  void 受講生更新ができること() throws Exception {
    // JSON形式のリクエストボディを直接指定
    String requestBody = """
        {
            "student": {
                "id": 1,
                "name": "name",
                "kanaName": "kanaName",
                "nickname": null,
                "email": "aaa@example.com",
                "livingArea": null,
                "age": 1,
                "gender": null,
                "remark": null,
                "deleted": false
            },
            "courses": [
                {
                    "id": 1,
                    "studentId": 1,
                    "startDate": "2000-01-01",
                    "endDueDate": "2000-12-31",
                    "courseId": 1
                }
            ]
        }
        """;

    mockMvc.perform(MockMvcRequestBuilders.put("/updateStudent")
            .contentType("application/json")
            .content(requestBody))
        .andExpect(status().isOk());

    verify(service, times(1)).updateStudent(any(StudentDetail.class));
  }

  @ParameterizedTest
  @CsvSource({
      // フィールドに不足がある場合（@Blank対象）
      "'', '', '', 1, 3, 'student.name, student.kanaName, student.email', "
          + "'must not be blank, must not be blank, must not be blank'",
      // フィールドに不正な値がある場合（@Email、@Positive対象）
      "'name', 'kanaName', 'aaa', 0, 2, 'student.email, student.age', "
          + "'must be a well-formed email address, must be greater than 0'"
  })
  void 受講生更新のバリデーションテスト(String name, String kanaName, String email, int age, int expectedErrorCount,
      String expectedErrorFields, String expectedErrorMessages) throws Exception {
    String requestBody = String.format("""
        {
            "student": {
                "id": 1,
                "name": "%s",
                "kanaName": "%s",
                "nickname": null,
                "email": "%s",
                "livingArea": null,
                "age": %d,
                "gender": null,
                "remark": null,
                "deleted": false
            },
            "courses": [
                {
                    "id": 1,
                    "studentId": 1,
                    "startDate": "2000-01-01",
                    "endDueDate": "2000-12-31",
                    "courseId": 1
                }
            ]
        }
        """,
        name,
        kanaName,
        email,
        age
    );

    mockMvc.perform(MockMvcRequestBuilders.put("/updateStudent")
            .contentType("application/json")
            .content(requestBody))
        .andExpect(status().isBadRequest()) // バリデーションエラーを期待
        .andExpect(result -> {
          // 例外を取得
          MethodArgumentNotValidException ex = (MethodArgumentNotValidException) result.getResolvedException();

          // BindingResultを取得し、すべてのエラーを確認
          BindingResult bindingResult = ex.getBindingResult();
          List<FieldError> fieldErrors = bindingResult.getFieldErrors();

          // バリデーションエラーが期待通りの数か確認
          assertThat(fieldErrors.size()).isEqualTo(expectedErrorCount);

          // フィールド名とエラーメッセージの配列を取得
          String[] expectedFields = expectedErrorFields.split(",");
          String[] expectedMessages = expectedErrorMessages.split(",");

          // 各フィールド名とメッセージをトリム（余分なスペースを削除）
          expectedFields = Arrays.stream(expectedFields).map(String::trim).toArray(String[]::new);
          expectedMessages = Arrays.stream(expectedMessages).map(String::trim).toArray(String[]::new);

          // 各フィールドのエラーメッセージを確認
          assertThat(fieldErrors)
              .extracting(FieldError::getField)
              .containsExactlyInAnyOrder(expectedFields);

          // 各フィールドのエラーメッセージ内容も確認
          assertThat(fieldErrors)
              .extracting(FieldError::getDefaultMessage)
              .containsExactlyInAnyOrder(expectedMessages);
        });

  }

  @Test
  void 受講生コース登録ができること() throws Exception {
    // JSON形式のリクエストボディを直接指定
    String requestBody = """
        {
            "studentId": 1,
            "courseId": 1
        }
        """;

    mockMvc.perform(MockMvcRequestBuilders.post("/registerStudentCourse")
            .contentType("application/json")
            .content(requestBody))
        .andExpect(status().isOk());

    verify(service, times(1)).registerStudentCourse(any(StudentCourse.class));
    verify(service, times(1)).searchCourseNameById(any(int.class));
  }

  @Test
  void 受講生コース登録で不正な値がある場合の例外処理が適切であること() throws Exception {
    // JSON形式のリクエストボディを直接指定（studentIdに0、courseIdに0を入れる）
    String requestBody = """
        {
            "studentId": 0,
            "courseId": 0
        }
        """;

    // 実行
    mockMvc.perform(MockMvcRequestBuilders.post("/registerStudentCourse")
            .contentType("application/json")
            .content(requestBody))
        .andExpect(status().isBadRequest()) // バリデーションエラーを期待
        .andExpect(result -> {
          // 例外を取得
          MethodArgumentNotValidException ex = (MethodArgumentNotValidException) result.getResolvedException();

          // BindingResultを取得し、すべてのエラーを確認
          BindingResult bindingResult = ex.getBindingResult();
          List<FieldError> fieldErrors = bindingResult.getFieldErrors();

          // バリデーションエラーが期待通りの数か確認
          assertThat(fieldErrors.size()).isEqualTo(2); // 2つのバリデーションエラーがあることを確認

          // 各フィールドのエラーメッセージを確認
          assertThat(fieldErrors)
              .extracting(FieldError::getField)
              .containsExactlyInAnyOrder("studentId", "courseId");

          // 各フィールドのエラー内容も確認可能
          assertThat(fieldErrors)
              .extracting(FieldError::getDefaultMessage)
              .contains("must be greater than 0", "must be greater than 0");
        });
  }

  @Test
  void コース登録ができること() throws Exception {
    // JSON形式のリクエストボディを直接指定
    String requestBody = """
        {
            "name": "name",
            "price": 1
        }
        """;

    mockMvc.perform(MockMvcRequestBuilders.post("/registerCourse")
            .contentType("application/json")
            .content(requestBody))
        .andExpect(status().isOk());

    verify(service, times(1)).registerCourse(any(Course.class));
  }

  @ParameterizedTest
  @CsvSource({
      "'', 1, name, 'must not be blank'", // nameのバリデーションエラー
      "'name', 0, price, 'must be greater than 0'" // priceのバリデーションエラー
  })
  void コース登録のバリデーションテスト(String name, int price, String expectedField, String expectedMessage) throws Exception {
    String requestBody = String.format("""
        {
            "name": "%s",
            "price": %d
        }
        """,
        name,
        price
    );

    // 実行
    mockMvc.perform(MockMvcRequestBuilders.post("/registerCourse")
            .contentType("application/json")
            .content(requestBody))
        .andExpect(status().isBadRequest()) // バリデーションエラーを期待
        .andExpect(result -> {
          // 例外を取得
          MethodArgumentNotValidException ex = (MethodArgumentNotValidException) result.getResolvedException();

          // BindingResultを取得し、すべてのエラーを確認
          BindingResult bindingResult = ex.getBindingResult();
          List<FieldError> fieldErrors = bindingResult.getFieldErrors();

          // バリデーションエラーが1つであることを確認
          assertThat(fieldErrors.size()).isEqualTo(1);

          // 各フィールドのエラーメッセージを確認
          assertThat(fieldErrors)
              .extracting(FieldError::getField)
              .containsExactly(expectedField);

          // 各フィールドのエラー内容も確認
          assertThat(fieldErrors)
              .extracting(FieldError::getDefaultMessage)
              .contains(expectedMessage);
        });
  }


  @Test
  void 受講生削除ができること() throws Exception {
    int id = 1;
    // モックの振る舞いを設定
    when(service.searchStudentById(id)).thenReturn(new Student(1, null, null, null, null, null, 1, null, null, false));
    mockMvc.perform(MockMvcRequestBuilders.patch("/deleteStudent/" + id))
        .andExpect(status().isOk());

    verify(service, times(1)).deleteStudent(id);
    verify(service, times(1)).searchStudentById(id);
  }

  @Test
  void 指定されたIDの受講生が存在しない場合に適切な例外処理が発生すること () throws Exception {
    int id = 1;
    doThrow(new ResourceNotFoundException("指定されたIDの受講生は存在しません")).when(service).deleteStudent(id);
    mockMvc.perform(MockMvcRequestBuilders.patch("/deleteStudent/" + id))
        .andExpect(status().isNotFound())
        .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException));
  }

  @Test
  void 指定されたIDの受講生が削除済みである場合に適切な例外処理が発生すること () throws Exception {
    int id = 1;
    doThrow(new ResourceConflictException("既に削除されている受講生です")).when(service).deleteStudent(id);
    mockMvc.perform(MockMvcRequestBuilders.patch("/deleteStudent/" + id))
        .andExpect(status().isConflict())
        .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceConflictException));
  }
}
