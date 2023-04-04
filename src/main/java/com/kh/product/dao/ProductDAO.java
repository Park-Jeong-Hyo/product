package com.kh.product.dao;

import java.util.List;
import java.util.Optional;

public interface ProductDAO {
  Long enroll(Product product);
  Optional<Product> checkById(Long pid);
  int update(Long pid, Product product);
  int delete(Long pid);
  List<Product> checkAll();
  boolean isExist(Long pid);
}
