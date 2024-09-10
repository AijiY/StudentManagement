package raisetech.student.management.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "削除結果を保持するクラス")
@Getter
@AllArgsConstructor
public class ResponseForDelete {
  private int id;
  private boolean deleted;

}
