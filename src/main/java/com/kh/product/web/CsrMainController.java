package com.kh.product.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/csr")
@Controller
public class CsrMainController {
  @GetMapping("/products")
  public String csrMain() {
    return "csr/product/csrMain";
  }
}
