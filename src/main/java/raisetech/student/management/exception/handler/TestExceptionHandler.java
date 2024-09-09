package raisetech.student.management.exception.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import raisetech.student.management.exception.ResourceConflictException;
import raisetech.student.management.exception.ResourceNotFoundException;

@ControllerAdvice
public class TestExceptionHandler {

  // @RequestParamに対するValidationのExceptionのハンドリング処理を記述（@Minなど対象）
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
    return ResponseEntity.badRequest().body(e.getMessage());
  }

  // @RequestBodyに対するValidationのExceptionのハンドリング処理を記述（@NotBlankなど対象）
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    Map<String, String> messages = e.getBindingResult().getFieldErrors().stream()
        .collect(
            java.util.stream.Collectors.toMap(
                org.springframework.validation.FieldError::getField,
                org.springframework.validation.FieldError::getDefaultMessage
            )
        );

    return ResponseEntity.badRequest().body(messages);
  }


  // @RequestParamのデータ型の不整合のエラーのハンドリング処理を記述
  @ExceptionHandler(TypeMismatchException.class)
  public ResponseEntity<Map<String, String>> handleTypeMismatchException(TypeMismatchException e) {
    Map<String, String> errorDetails = new HashMap<>();

    String fieldName = "unknown";  // デフォルト値
    String requiredType = "unknown";
    String inputValue = "unknown";

    // エラーメッセージから詳細情報を抽出
    if (e instanceof MethodArgumentTypeMismatchException) {
      MethodArgumentTypeMismatchException typeMismatchException = (MethodArgumentTypeMismatchException) e;
      fieldName = typeMismatchException.getName();
      requiredType = typeMismatchException.getRequiredType().getSimpleName();
      inputValue = typeMismatchException.getValue() != null ? typeMismatchException.getValue().toString() : "null";
    }

    String message = String.format("フィールド '%s' の型は '%s' ですが、'%s' が入力されています。",
        fieldName, requiredType, inputValue);

    errorDetails.put("error", message);

    return ResponseEntity.badRequest().body(errorDetails);
  }

  // @RequestBodyのJSONのパースエラーのハンドリング処理を記述
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Map<String, String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
    Map<String, String> errorDetails = new HashMap<>();
    String message = "入力形式が不正です。";

    // エラーメッセージの詳細を解析
    if (e.getCause() instanceof InvalidFormatException) {
      InvalidFormatException ife = (InvalidFormatException) e.getCause();
      String fieldName = ife.getPath().get(0).getFieldName();
      String requiredType = ife.getTargetType().getSimpleName();
      String value = ife.getValue() != null ? ife.getValue().toString() : "null";
      message = String.format("フィールド '%s' の型は '%s' ですが、'%s' が入力されています。",
          fieldName, requiredType, value);
    }
    errorDetails.put("error", message);
    return ResponseEntity.badRequest().body(errorDetails);
  }

  // insert時のid重複エラーのハンドリング処理を記述
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body("IDが重複しています");
  }

  // リソースの競合エラーのハンドリング処理を記述（例：論理削除済みのデータを論理削除しようとした場合）
  @ExceptionHandler(ResourceConflictException.class)
  public ResponseEntity<String> handleResourceConflictException(ResourceConflictException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
  }

  // リソースが見つからないエラーのハンドリング処理を記述
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
  }


}

