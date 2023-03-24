package com.kh.product.svc;

import com.kh.product.dao.Product;
import com.kh.product.dao.ProductDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductSVCImpl  implements ProductSVC{
  private final ProductDAO productDAO;

  @Override
  public Long enroll(Product product) {
    return productDAO.enroll(product);
  }

  @Override
  public Optional<Product> checkById(Long pid) {
    return productDAO.checkById(pid);
  }

  @Override
  public int update(Long pid, Product product) {
    return productDAO.update(pid, product);
  }

  @Override
  public int delete(Long pid) {
    return productDAO.delete(pid);
  }

  @Override
  public List<Product> checkAll() {
    return productDAO.checkAll();
  }
}
