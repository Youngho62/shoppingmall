package com.web.controller;

import com.web.domain.Product;
import com.web.repository.ProductFilesRepository;
import com.web.repository.ProductRepository;
import com.web.vo.PageMaker;
import com.web.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductFilesRepository productFilesRepository;


    @GetMapping("/list")
    public void list(@ModelAttribute("pageVO") PageVO vo, Model model){

        Pageable page = vo.makePageable(0, "pno");

        Page<Product> result = productRepository.findAll(productRepository.makePredicate(vo.getKeyword()), page);
        model.addAttribute("result", new PageMaker<>(result));

    }

    @GetMapping("/view")
    public void view(Long pno, @ModelAttribute("pageVO") PageVO pageVO,Model model){
        productRepository.findById(pno).ifPresent(product ->{
            product.setPHit(product.getPHit()+1);
            productRepository.save(product);
            model.addAttribute("product",product);
        });
    }

    @GetMapping("/register")
    public void register(){

    }
    @Transactional
    @PostMapping("/register")
    public String registerPost(@ModelAttribute("product") Product product, RedirectAttributes rttr) {
        //업로드 파일이 존재하면
        if (product.getFiles() != null) {
            AtomicInteger cnt = new AtomicInteger();
            product.getFiles().forEach(productFiles -> {
                productFiles.setProduct(product);
                //메인 파일을 선택했으면 저장
                if (productFiles.isMainPic()) {
                    product.setMainPic(productFiles.getUploadUrl() + "\\" + productFiles.getUuid() + "_" + productFiles.getFileName());
                    cnt.addAndGet(1);
                }
                ;
                productFilesRepository.save(productFiles);
            });
            //파일은 업로드 했는데 메인파일을 선택하지않았으면
            if (cnt.get() == 0) {
                product.setMainPic("exefile.png");
            }
        }
        //파일업로드를 아예 하지않았으면
        else {
            product.setMainPic("exefile.png");
        }
        productRepository.save(product);
        rttr.addFlashAttribute("msg", "success");
        return "redirect:/product/list";
    }


    @GetMapping("/modify")
    public void modify(Long pno,@ModelAttribute("pageVO")PageVO pageVO,Model model){
        productRepository.findById(pno).ifPresent(product -> model.addAttribute("product",product));
    }
    @Transactional
    @PostMapping("/modify")
    public String modifyPost(Product product, PageVO vo, RedirectAttributes rttr ){
        productRepository.findById(product.getPno()).ifPresent( origin ->{
            if(product.getFiles()!=null){
                productFilesRepository.deleteAllByProduct(origin);
                product.getFiles().forEach(file->{
                    file.setProduct(product);
                    productFilesRepository.save(file);
                });
            }
            origin.setName(product.getName());
            origin.setPrice(product.getPrice());
            origin.setCategory(product.getCategory());
            origin.setUpdateDate(product.getUpdateDate());
            origin.setDescription(product.getDescription());
            productRepository.save(origin);
            rttr.addFlashAttribute("msg", "modifysuccess");
            rttr.addAttribute("pno", origin.getPno());
        });

        rttr.addAttribute("page", vo.getPage());
        rttr.addAttribute("size", vo.getSize());
        rttr.addAttribute("keyword", vo.getKeyword());

        return "redirect:/product/view";
    }
    @Transactional
    @PostMapping("/delete")
    public String delete(Long pno, PageVO vo, RedirectAttributes rttr ){
        productFilesRepository.deleteAllByProduct(productRepository.findById(pno).get());
        productRepository.deleteById(pno);
        rttr.addFlashAttribute("msg", "deletesuccess");
        //페이징과 검색했던 결과로 이동하는 경우
        rttr.addAttribute("page", vo.getPage());
        rttr.addAttribute("size", vo.getSize());
        rttr.addAttribute("keyword", vo.getKeyword());

        return "redirect:/product/list";
    }

}
