package com.kh.product.web.Rest;

import lombok.Data;

@Data
public class UpdateRest {
  private Long pid;
  private String pname;
  private Long quantity;
  private Long price;
}
