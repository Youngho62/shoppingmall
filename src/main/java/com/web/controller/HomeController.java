package com.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/cart")
    public void cart(){

    }
    @GetMapping("/checkout")
    public void checkout(){

    }
    @GetMapping("/")
    public String index(){
        return "/index";
    }
    @GetMapping("/productDetails")
    public void product_details(){

    }
}
