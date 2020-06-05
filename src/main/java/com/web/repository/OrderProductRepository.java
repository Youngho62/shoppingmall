package com.web.repository;

import com.web.domain.Order;
import com.web.domain.OrderProduct;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderProductRepository extends CrudRepository<OrderProduct,Long> {
    List<OrderProduct> findAllByOrder(Order order);
}
