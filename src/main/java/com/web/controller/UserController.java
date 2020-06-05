package com.web.controller;

import com.web.domain.Order;
import com.web.domain.Product;
import com.web.domain.QnA;
import com.web.domain.User;
import com.web.repository.OrderRepository;
import com.web.repository.QnARepository;
import com.web.repository.UsersRepository;
import com.web.security.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private CustomUserDetailService customUserDetailService;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private QnARepository qnARepository;

    @GetMapping("/loginForm")
    public void loginForm(){

    }
    @GetMapping("/signUp")
    public void signUp(){

    }
    @PostMapping("/signUp")
    public String postSignUp(User user) {
        customUserDetailService.save(user);
        return "redirect:/users/loginForm";
    }

    @GetMapping("/idCheck")
    @ResponseBody
    public int idCheck(@RequestParam("userId") String userId){
        int num=usersRepository.findCountByUserId(userId);
        return num;
    }

    @GetMapping("/myPage")
    public void myPage(Long uNum, Model model){
        User user=usersRepository.findById(uNum).get();
        List<Order> orders= orderRepository.findAllByUser(user);
        List<QnA> qnas=qnARepository.findAllSortByRegdate(user.getUserId(),5);
        int cnt=qnARepository.findAllCountByWriter(user.getUserId());
        model.addAttribute("count",cnt);
        model.addAttribute("user",user);
        model.addAttribute("orders",orders);
        model.addAttribute("qnas",qnas);
    }

    @GetMapping("modify")
    public void modify(Long uNum, Model model){
        User user=usersRepository.findById(uNum).get();
        model.addAttribute("user",user);
    }
    @PostMapping("modify")
    public String  postModify(User newUser){
        User user= usersRepository.findById(newUser.getUNum()).get();
        if(newUser.getUserPw()!=null) {
            user.setUserPw(passwordEncoder.encode(newUser.getUserPw()));
        }
        user.setPostNum(newUser.getPostNum());
        user.setAddr1(newUser.getAddr1());
        user.setAddr2(newUser.getAddr2());
        user.setUserPhone(newUser.getUserPhone());
        user.setUserEmail(newUser.getUserEmail());
        usersRepository.save(user);
        return "redirect:/users/myPage?uNum="+user.getUNum();
    }

    @PostMapping("pwCheck")
    @ResponseBody
    public boolean pwCheck(Long uNum,String pw){
        User user= usersRepository.findById(uNum).get();
        return passwordEncoder.matches(pw,user.getUserPw());
    }

    @GetMapping("/modifyPoint")
    @ResponseBody
    public void modifyPoint(Long uNum,int point){
        User user=usersRepository.findById(uNum).get();
        user.setPoint(user.getPoint()-point);
        usersRepository.save(user);
    }
}

