package com.kh.product.web;

import com.kh.product.dao.Product;
import com.kh.product.svc.ProductSVC;
import com.kh.product.web.form.CheckForm;
import com.kh.product.web.form.EnrollForm;
import com.kh.product.web.form.UpdateForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
  private final ProductSVC productSVC;

  //등록 양식
  @GetMapping("/add")
  // 빈(empty)객체를 생성하여 view에 전달, 렌더링 할 때 오류가 나지 않도록
  // 아무 객체도 전달되지 않으면 view에 있는 타임리프 코드에서 오류가 발생한다.
  public String enrollForm(Model model) {
    // view단에 보내는 속성을 넣을 객체 enrollForm
    EnrollForm enrollForm = new EnrollForm();
    // 속성을 model 객체에 넣어서 view에 전달
    // 비어있기 때문에, 실질적으로 아무것도 전달하지 않는다. 연결을 목적으로 한다.
    // enrollForm.html에서 thymeleaf를 사용(th:object...), view와 controller를 연결
    model.addAttribute("enrollForm", enrollForm);
    return "product/enrollForm";
  }
  //등록
  @PostMapping("/add")
  public String save(
      // valid: 유효성 검사
      // modelAttribut 요청 데이터(사용자가 입력한)를 자바 객체로 바인딩, 모델 객체에 추가
      // 타임리프에 th:object로 view와 컨트롤러가 연결되었고, 매개변수에
      // 요청 데이터가 들어와서, 그걸 자바객체에 바인딩, 모델객체에 추가함.
      // 이때 연결되는 건 th:object="${enrollForm}", 타입이 연결되는 것이고, 변수명은 해당하지 않는다.
      // 사용자가 입력한 정보가 타임리프의 th:object.. 코드를 통해서 매개변수의 형태로 지금 전달이 된 것
      @Valid @ModelAttribute EnrollForm enrollForm,
      // 유효성 검사 결과를 담는 객체
      BindingResult bindingResult,
      //등록결과 나온 id를 url주소의 id값에 넣어서 리다이렉트 하기 위함
      RedirectAttributes redirectAttributes
  ) {
    //검증
    // validation 어노테이션기반 검증
    if(bindingResult.hasErrors()) {
      log.info("bindingResult={}", bindingResult);
      return "product/enrollForm";
    }
    //필드 오류
    if(enrollForm.getQuantity() == 100) {
      //오류필드명, 오류코드, 사용자 입력값
      bindingResult.rejectValue("quantity", "product");
    }
    // 글로벌 오류
    // 총액 (상품수량*단가) 1000만원 초과 금지
    if(enrollForm.getQuantity() * enrollForm.getPrice() > 10_000_000L) {
      //오브젝트 에러(오류코드, 사용자 입력값)
      // errorCode,errorArgs: 오류메시지에서 {0}을 치환하기위한 값, defaultmessage
      bindingResult.reject("totalprice", new String[]{"1000"}, "");
    }

    if(enrollForm.getQuantity() > 1 && enrollForm.getQuantity() < 10) {
      bindingResult.reject("quantity", new String[]{"1", "2"}, "");
    }

    if(bindingResult.hasErrors()) {
      log.info("bindingResult={}", bindingResult);
      return "product/enrollForm";
    }

    //등록
    // DB에 전달할 product객체에 사용자가 입력하여 자바객체에 바인딩 된 enrollForm의 정보가 입력된다.
    Product product = new Product();
    product.setPname(enrollForm.getPname());
    product.setQuantity(enrollForm.getQuantity());
    product.setPrice(enrollForm.getPrice());

    //SVC에 전달,
    //전달하고 나면 svc를 거쳐서 dao로 전달되고 로직이 처리가 되서 pid라는 것을 반환한다.
    // pid라는 형태로 svc에 전달이 되고 다시 컨트롤러로 정보가 전달이 된다.
    Long enrolledPid = productSVC.enroll(product);

    //id를 경로 url에 전달하기 위한 코드
    redirectAttributes.addAttribute("id", enrolledPid);
    return "redirect:/products/{id}/check";
  }
  //조회
  @GetMapping("/{id}/check")
  public String checkById(
      //{id}부분을 매개변수로 사용하겠다는 선언
      @PathVariable("id") Long id,
      Model model
  ) {
    // 조회 결과가 없을 경우 예외 처리를 간단하게 하기 위한 Optional 클래스
    // id값을 SVC에 전달, 전달 결과를 foundProduct에 대입
    Optional<Product> foundProduct = productSVC.checkById(id);
    // id값으로 조회한 결과 값이 null 일 경우 예외를 발생시킨다.
    Product product = foundProduct.orElseThrow();

    CheckForm checkForm = new CheckForm();
    checkForm.setPid(product.getPid());
    checkForm.setPname(product.getPname());
    checkForm.setQuantity(product.getQuantity());
    checkForm.setPrice(product.getPrice());
    //view(checkform.html)에 th:object="${checkform}"의 형태로 전달하기 위함
    model.addAttribute("checkForm", checkForm);
    return "product/checkForm";
  }
  //수정양식
  @GetMapping("/{id}/edit")
  public String updateForm(
      @PathVariable("id") Long id,
      Model model
  ) {
    Optional<Product> foundProduct = productSVC.checkById(id);
    Product product = foundProduct.orElseThrow();

    UpdateForm updateForm = new UpdateForm();
    updateForm.setPid(product.getPid());
    updateForm.setPname(product.getPname());
    updateForm.setQuantity(product.getQuantity());
    updateForm.setPrice(product.getPrice());
    model.addAttribute("updateForm",updateForm);
    return "product/updateForm";
  }
  // 수정
  @PostMapping("{id}/edit")
  public String update(
      @PathVariable("id") Long pid,
      @ModelAttribute UpdateForm updateForm,
      BindingResult bindingResult,
      RedirectAttributes redirectAttributes
      ) {
    if(bindingResult.hasErrors()) {
      log.info("bindingResult={}", bindingResult);
      return "product/updateForm";
    }
    //데이터 검증
    if(bindingResult.hasErrors()) {
      log.info("bindingResult={}", bindingResult);
      return "product/updateForm";
    }

    //수정처리
    Product product = new Product();
    product.setPid(pid);
    product.setPname(updateForm.getPname());
    product.setQuantity(updateForm.getQuantity());
    product.setPrice(updateForm.getPrice());

    redirectAttributes.addAttribute("id", pid);
    return "redirect:/product/{id}/check";
  }
  // 삭제
  @GetMapping("/{id}/del")
  public String delete(
      @PathVariable("id") Long pid
  ) {
    //컨트롤러에 id만 전달
    productSVC.delete(pid);
    return "redirect:/products";
  }
  @GetMapping
  // 목록
  public String checkAll(Model model) {
    List<Product> products = productSVC.checkAll();
    model.addAttribute("products", products);
    return "product/checkAll";
  }
}
