package com.web.repository;

import com.web.domain.Order;
import com.web.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order,Long> {
    List<Order> findAllByUser(User user);
}
