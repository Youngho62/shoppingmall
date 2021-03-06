package com.web.controller;

import com.web.domain.Cart;
import com.web.domain.Product;
import com.web.domain.User;
import com.web.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UsersRepository usersRepository;

    @GetMapping("/list")
    public void list(Principal principal, Model model){
        User user=usersRepository.findUserByUserId(principal.getName());
        List<Cart> carts=cartRepository.findAllByUser(user);
        AtomicInteger amount= new AtomicInteger();
        carts.forEach(cart -> {
            Product product=cart.getProduct();
            amount.addAndGet(product.getPrice()*cart.getCount());
        });
        model.addAttribute("user",user);
        model.addAttribute("carts",carts);
        model.addAttribute("amount",amount);
    }

    @PostMapping("/addCart")
    @ResponseBody
    public String addCart(Cart cart,Long pno,Long uNum){
        AtomicBoolean result= new AtomicBoolean(true);
        cartRepository.findAll().forEach(originCart->{
            //장바구니의 유저 정보가 같고 기존 장바구니에 있던 제품일 경우엔 갯수만 증가
            if(originCart.getUser().getUNum().equals(uNum)&&originCart.getProduct().getPno().equals(pno)){
                originCart.setCount(originCart.getCount()+1);
                cartRepository.save(originCart);
                result.set(false);
            }
        });
        if(result.get()){
            cart.setCount(1);
            cart.setProduct(productRepository.findById(pno).get());
            cart.setUser(usersRepository.findById(uNum).get());
            cartRepository.save(cart);
        }
        return "장바구니에 성공정으로 담았습니다.";
    }
    //카운트를 포함한 메소드
    @PostMapping("/addCountCart")
    public String addCountCart(Cart cart,Long pno,Long uNum){
        AtomicBoolean result= new AtomicBoolean(true);
        cartRepository.findAll().forEach(originCart->{
            //장바구니의 유저 정보가 같고 기존 장바구니에 있던 제품일 경우엔 갯수만 증가
            if(originCart.getUser().getUNum().equals(uNum)&&originCart.getProduct().getPno().equals(pno)){
                originCart.setCount(originCart.getCount()+cart.getCount());
                cartRepository.save(originCart);
                result.set(false);
            }
        });
        if(result.get()){
            cart.setProduct(productRepository.findById(pno).get());
            cart.setUser(usersRepository.findById(uNum).get());
            cartRepository.save(cart);
        }
        return "redirect:/cart/list?uNum="+uNum;
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public String delete(Long cno){
        cartRepository.deleteById(cno);
        return "deletesuccess";
    }

    @GetMapping("/amount")
    @ResponseBody
    public int getAmount(String cno,String qty,String price,int opt){
        Cart cart=cartRepository.findById(Long.parseLong(cno)).get();
        int chQty=Integer.parseInt(qty);
        cart.setCount(opt==0?chQty-1:chQty+1);
        cartRepository.save(cart);
        if(opt==0&&chQty==1){
            cartRepository.deleteById(Long.parseLong(cno));
        }
        int chPrice=Integer.parseInt(price);

        return opt==0?(chQty-1)*chPrice:(chQty+1)*chPrice;
    }


    @PostMapping("/postPayment")
    public String postPayment(Long uNum,int total, RedirectAttributes rttr){
        System.out.println(uNum+total+"☆★☆★☆★☆★☆★☆★☆★");
        User user=usersRepository.findById(uNum).get();
        List<Cart> carts=cartRepository.findAllByUser(user);
        AtomicInteger amount= new AtomicInteger();
        carts.forEach(cart -> {
            Product product=cart.getProduct();
            amount.addAndGet(product.getPrice()*cart.getCount());
        });
        rttr.addFlashAttribute("total",total);
        rttr.addFlashAttribute("amount",amount);
        rttr.addFlashAttribute("user",user);
        rttr.addFlashAttribute("carts",carts);

        return "redirect:/cart/payment";
    }

    @GetMapping("payment")
    public void payment(){

    }


}
