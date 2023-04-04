package com.kh.product.web.Rest;

import lombok.Data;

//등록에 필요한 3가지 속성을 정의
@Data
public class EnrollRest {
  private String pname;
  private Long quantity;
  private Long price;
}
