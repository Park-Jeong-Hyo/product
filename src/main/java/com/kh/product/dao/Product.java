package com.kh.product.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //getter, setter, etc
@AllArgsConstructor// 모든 맴버필드를 매개변수로하는 생성자 생성
@NoArgsConstructor // 디폴트 생성자
public class Product {
  private Long pid;
  private String pname;
  private Long quantity;
  private Long price;
}
