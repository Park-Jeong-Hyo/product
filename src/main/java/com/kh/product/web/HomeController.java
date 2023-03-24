package com.kh.product.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// 메인페이지 매핑
@Controller
@RequestMapping("/")
public class HomeController {
  @GetMapping
  public String home() {
    return "home";
  }
}
