package com.web.controller;

import com.web.domain.*;
import com.web.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RequestMapping("/order")
@Controller
public class OrderController {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderProductRepository orderProductRepository;
    @Autowired
    private CartRepository cartRepository;


    @PostMapping("/complete")
    @ResponseBody
    public String complete(Order order, Long uNum, Long[] pno,int[] count){
        order.setUser(usersRepository.findById(uNum).get());

        List<OrderProduct> result=new ArrayList<>();
        for(int i=0; i<pno.length;i++){
            OrderProduct orderProduct=new OrderProduct();
            orderProduct.setProduct(productRepository.findById(pno[i]).get());
            orderProduct.setCount(count[i]);
            orderProduct.setOrder(order);
            orderProductRepository.save(orderProduct);
            result.add(orderProduct);
        }

        orderRepository.save(order);

        User user=usersRepository.findById(uNum).get();
        List<Cart> carts=cartRepository.findAllByUser(user);
        carts.forEach(cart -> {
            cartRepository.delete(cart);
        });
        return "/order/completeView";
    }
    @GetMapping("completeView")
    public void completeView(){

    }
}
