package com.kh.product.web.form;

import lombok.Data;

@Data
public class CheckForm {
  private Long pid;
  private String pname;
  private Long quantity;
  private Long price;
}
