package com.kh.product.web.form;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class EnrollForm {
  @NotBlank //null과 빈문자열("")을 허용하지 않음, 문자열 타입만 가능
  @Size(min=2,max=10)
  private String pname;
  @NotNull
  @Positive //양수
  @Max(1000) //최대값
  private Long quantity;
  @NotNull
  @Positive //양수
  @Min(1000) //최소값
  private Long price;
  //@NotEmpty: null, 빈문자열(""), 공백문자(" ") 허용안함,
  //문자열, 컬렉션타입(요소가 1개이상 존재)에 사용
}
