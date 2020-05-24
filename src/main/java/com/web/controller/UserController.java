package com.web.controller;

import com.web.domain.User;
import com.web.repository.UsersRepository;
import com.web.security.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private CustomUserDetailService customUserDetailService;
    @Autowired
    private UsersRepository usersRepository;

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
}

