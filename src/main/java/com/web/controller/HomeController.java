package com.web.controller;

import com.web.domain.Product;
import com.web.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/")
    public String index(Model model){
        List<Product> products=productRepository.findAllSortByPHit(11);
        model.addAttribute("products",products);
        return "index";
    }
}
