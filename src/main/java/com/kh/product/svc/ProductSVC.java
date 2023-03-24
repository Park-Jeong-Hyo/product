package com.kh.product.svc;

import com.kh.product.dao.Product;

import java.util.List;
import java.util.Optional;

public interface ProductSVC {
  Long enroll(Product product);
  Optional<Product> checkById(Long pid);
  int update(Long pid, Product product);
  int delete(Long pid);
  List<Product> checkAll();
}
