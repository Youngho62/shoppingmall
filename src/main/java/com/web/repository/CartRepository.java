package com.web.repository;

import com.web.domain.Cart;
import com.web.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CartRepository extends CrudRepository<Cart,Long> {
    List<Cart> findAllByUser(User user);
}
