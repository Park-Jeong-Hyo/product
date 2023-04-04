package com.kh.product.web;

import com.kh.product.dao.Product;
import com.kh.product.svc.ProductSVC;
import com.kh.product.web.Rest.EnrollRest;
import com.kh.product.web.Rest.UpdateRest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController //@Controller + @ResponseBody
@RequiredArgsConstructor //data안에 RequiredArgsConstructor가 포함되어 있기는 하다.
@RequestMapping("/rest/")
public class RestProductController {

  //control0ler와 svc를 연결하기 위함
  private final ProductSVC productSVC;

  //등록
  //@RequestBody: 클라이언트가 전송하는 json형태의 요청메세지를 자바 객체로 변환
  //ssr에서 사용했던 form을 controller의 정보를 view에 전달하기 위해서 사용했던 것 처럼
  // 마찬가지로 json을 자바객체로 바꾸기 위한 Rest를 정의할 필요가 있다.
  // 반환타입이 RestResponse<Object>로 설정되었다.
  @PostMapping
  public RestResponse<Object> save(@RequestBody EnrollRest enrollRest) {
    RestResponse<Object> result = null;
    // 검증

    // 등록
    Product product = new Product();
    product.setPname(enrollRest.getPname());
    product.setQuantity(enrollRest.getQuantity());
    product.setPrice(enrollRest.getPrice());

    //서버에 전달하고, pid를 불러운다.
    Long pid = productSVC.enroll(product);
    //불러온 pid를 product객체에 넣는다.
    product.setPid(pid);
    // 클라이언트로 부터 입력받은 데이터를 DB에 전달해서, 등록을 완료한 상태이다.

    //메세지 생성
    //pid가 0보다 크다는 것은 로직이 제대로 작동했다는 뜻이다.
    //createResponse의 매개변수, 각각 코드, 메세지, 데이터 타입이다.
    if(pid > 0) {
      result = RestResponse.createResponse("01", "성공", product);
    } else {
      result = RestResponse.createResponse("02", "실패", "서버오류");
    }
    return result;
    // 클라이언트에는 결과 메세지를 전달한다.
  }

  //개별조회
  @GetMapping("/{id}")
  public RestResponse<Object> checkById(@PathVariable("id") Long pid) {
    RestResponse<Object> result = null;

    if(!productSVC.isExist(pid)) {
      result = RestResponse.createResponse("03", "해당 상품이 없음", null);
      return result;
    }
    Optional<Product> product = productSVC.checkById(pid);
    return result = RestResponse.createResponse("01", "성공", product);
  }

  //수정
  @PatchMapping("/{id}")
  public RestResponse<Object> update(
      @PathVariable("id") Long pid,
      @RequestBody UpdateRest updateRest
      ) {
    RestResponse<Object> result = null;

    // 간단한 검증로직, 해당 pid가 존재하는지 확인
    if(!productSVC.isExist(pid)) {
      result = RestResponse.createResponse("03", "해당 상품이 없음", null);
      return result;
    }

    //수정
    Product product = new Product();
    product.setPname(updateRest.getPname());
    product.setQuantity(updateRest.getQuantity());
    product.setPrice(updateRest.getPrice());

    int updateCnt = productSVC.update(pid, product);
    updateRest.setPid(pid);

    //콘솔에 찍어보면 1이 나올 경우 성공이라는 걸 알 수 있음.
    if(updateCnt == 1) {
      result = RestResponse.createResponse("01","성공",updateRest);
    } else {
      result = RestResponse.createResponse("02", "실패", null);
    }
    return result;
  }
  //삭제
  @DeleteMapping("/{id}")
  public RestResponse<Object> delete(
      @PathVariable("id") long pid
  ) {
    RestResponse<Object> result = null;
    //예외 처리 로직
    if(!productSVC.isExist(pid)) {
      result = RestResponse.createResponse("03", "해당 상품이 없음", null);
    }

    //삭제 실행
    int deleteCtn = productSVC.delete(pid);

    if(deleteCtn == 1) {
      result = RestResponse.createResponse("01","성공", null);
    } else {
      result = RestResponse.createResponse("02","실패", "서버오류");
    }

    return result;
  }
  //전체조회
  @GetMapping
  public RestResponse<Object> checkAll() {
    RestResponse<Object> result = null;
    List<Product> list = productSVC.checkAll();
    //list.size(): 리스트 안에 들어있는 요소의 갯수를 반환
    if(list.size() > 1) {
      result = RestResponse.createResponse("01","성공", list);
    } else {
      result = RestResponse.createResponse("03","조회 결과 없음", null);
    }
    return result;
  }
}
