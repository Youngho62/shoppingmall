package com.web.controller;

import com.web.domain.Cart;
import com.web.domain.Order;
import com.web.domain.OrderProduct;
import com.web.domain.User;
import com.web.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

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
        User user = usersRepository.findById(uNum).get();
        //결제 완료를 하면 10%를 포인트로 지급
        user.setPoint(user.getPoint() + (int) ((order.getTotalPrice() <100000?order.getTotalPrice()-2500 :order.getTotalPrice() )* 0.1));

        List<Cart> carts=cartRepository.findAllByUser(user);
        carts.forEach(cart -> {
            cartRepository.delete(cart);
        });
        return "/order/completeView";
    }
    @GetMapping("completeView")
    public void completeView(){

    }

    @GetMapping("/view")
    public void view(Long ono, Model model){
        System.out.println(ono);
        Order order=orderRepository.findById(ono).get();
        System.out.println(order);
        List<OrderProduct> products=orderProductRepository.findAllByOrder(order);
        System.out.println(products);
        model.addAttribute("user",order.getUser());
        model.addAttribute("products",products);
        model.addAttribute("order",order);
    }
}
