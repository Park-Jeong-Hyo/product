package com.kh.product.web;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
//헤더에는 내가 설정한 메세지가, data에는 requestbody가 출력된다.
//객체안에 객체가 있는 형태로 메세지가 출력된다.
//data의 타입이 product일 수도, String일 수도 있기 때문에 제네릭을 사용했다.
public class RestResponse<T> {
  private Header header;
  private T data;

  // 이너클래스
  @Data
  @AllArgsConstructor
  public static class Header {
    private String code;
    private String message;
  }

  //제네릭 타입 <T>, 반환타입 RestResponse<T>
  public static <T> RestResponse<T> createResponse(String code, String message, T data) {
   return new RestResponse<>(new Header(code, message), data);
  }
}
