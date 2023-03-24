package com.kh.product.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class ProductDAOImplTest {
  @Autowired
  ProductDAO productDAO;
  //조회
  @Test
  @DisplayName("상품조회")
  void checkById() {
    Long pid = 11L;
    Optional<Product> product = productDAO.checkById(pid);
    Product findedProduct = product.orElseThrow();// 없으면 java.util.NoSuchElementException
    Assertions.assertThat(findedProduct.getPname()).isEqualTo("프린터1");
    Assertions.assertThat(findedProduct.getQuantity()).isEqualTo(10L);
    Assertions.assertThat(findedProduct.getPrice()).isEqualTo(3000L);

  }
}
