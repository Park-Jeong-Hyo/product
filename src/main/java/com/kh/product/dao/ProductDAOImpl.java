package com.kh.product.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ProductDAOImpl implements ProductDAO{
  // 파라미터를 ? 가 아닌 이름으로 연결하는 NamedParameterJdbcTemplate
  private final NamedParameterJdbcTemplate template;

  @Override
  public Long enroll(Product product) {
    //가변 객체
    StringBuffer sb = new StringBuffer();
    //마지막 따옴표 앞 띄워주기 + 세미콜론 삭제
    sb.append("insert into product(pid,pname,quantity,price) ");
    sb.append("values(product_pid_seq.nextval, :pname ,:quantity, :price) ");
    // 데이터베이스에서 생성되는 primary key를 구하고자 할 때
    KeyHolder keyHolder = new GeneratedKeyHolder();
    //파라미터를 Map객체에 저장, 인터페이스 sqlParameter의 구현 beanPropertyParameterSouce
    SqlParameterSource param = new BeanPropertySqlParameterSource(product);
    //DB등록/ 메소드(1. sql문자열 2. map같은 객체 3. 자동으로 증가하는 키값, 4. 칼럼 명
    template.update(sb.toString(), param,  keyHolder, new String[]{"pid"});
    //등록한 상품을 조회하는 화면으로 돌아가기 위한 상품 아이디
    Long pid = keyHolder.getKey().longValue();
    return pid;
  }

  @Override
  public Optional<Product> checkById(Long pid) {
    //가변 객체 생성
    StringBuffer sb = new StringBuffer();
    //sql 구문 작성
    sb.append("select pid, pname, quantity, price ");
    sb.append("  from product ");
    sb.append(" where pid = :id ");
    try {
      // map객체 param에 키를 "pid", 값 "pid"대입
      Map<String, Long> param = Map.of("id", pid);

      //1. sql 구문, 2. map객체, 3. 임의 리턴 클래스
      Product product = template.queryForObject(sb.toString(),param, BeanPropertyRowMapper.newInstance(Product.class));
      // null 이 아니면 결과값 Optional.of()를 반환
      log.info("product={}", product);
      return Optional.of(product);
      //결과값이 없거나 2개 이상인 경우 queryForObject는 오류를 반환
    } catch (EmptyResultDataAccessException e) {
      // 결과값이 null인 경우
      return Optional.empty();
    }
  }

  @Override
  public int update(Long pid, Product product) {
    StringBuffer sb = new StringBuffer();
    sb.append("update product ");
    sb.append("set pname = :pname, ");
    sb.append("    quantity = :quantity, ");
    sb.append("    price = :price ");
    sb.append("where pid = :id ");

    //sqlparametersource 인터페이스의 구현 mapsqlparametersource
    // beanproperty.. 은 자바 빈 규약을 따르는 map
    // mapsql.. 은 맵 객체
    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("pname", product.getPname())
        .addValue("quantity", product.getQuantity())
        .addValue("price",product.getPrice())
        .addValue("id",pid);
    return template.update(sb.toString(), param);
  }

  @Override
  public int delete(Long pid) {
    String sql = "delete from product where pid = :id ";
    int result = template.update(sql, Map.of("id", pid));
    //결과값이 1이 나오면 성공인 것을 알 수 있음.
    log.info("result={}", result);
    return result;
  }

  @Override
  public List<Product> checkAll() {
    StringBuffer sb = new StringBuffer();
    sb.append("select pid, pname, quantity, price ");
    sb.append("from product ");

    List<Product> list = template.query(sb.toString(), BeanPropertyRowMapper.newInstance(Product.class));
    return list;
  }

  @Override
  public boolean isExist(Long pid) {
    boolean isExist = false;
    StringBuffer sql = new StringBuffer();
    // 1아니면 0이 도출된다. 존재하면 1 없으면 0이다.
    sql.append("select count(*) from product where pid = :pid ");
    Map<String,Long> param = Map.of("pid", pid);
    Integer integer = template.queryForObject(sql.toString(), param, Integer.class);
    isExist = (integer > 0) ? true : false;
    return isExist;
  }
}
