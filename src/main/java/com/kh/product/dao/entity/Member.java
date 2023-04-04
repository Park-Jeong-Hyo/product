package com.kh.product.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Member {
  private String mname;
  private String mid;
  private String pw;
  private String phone;
  private String email;
  private String nickname;
  private String maddress;
  private String mtype;
  private String companyname;
  private String businessnumber;
}
