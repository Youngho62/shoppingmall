package com.web.repository;

import com.web.domain.OrderProduct;
import org.springframework.data.repository.CrudRepository;

public interface OrderProductRepository extends CrudRepository<OrderProduct,Long> {
}
