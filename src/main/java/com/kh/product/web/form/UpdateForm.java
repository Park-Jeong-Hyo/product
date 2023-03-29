package com.kh.product.web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateForm {
  private Long pid;
  @NotBlank //null이어서는 안되고, 적어도 하나의 non-whtiespace 띄어쓰기를 포함해야함.
  private String pname;
  @NotNull  //null이어서는 안되고, 어떤 타입이든 가능
  private Long quantity;
  @NotNull
  private Long price;
}
