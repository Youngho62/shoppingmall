package com.web.controller;

import com.web.domain.QnA;
import com.web.repository.QnARepository;
import com.web.vo.PageMaker;
import com.web.vo.PageVO;
import lombok.extern.java.Log;
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

@Controller
@Log
@RequestMapping("/qna")
public class QnAController {
    @Autowired
    private QnARepository qnARepository;
    @GetMapping("/list")
    public void list(@ModelAttribute("pageVO") PageVO vo, Model model){

        Pageable page = vo.makePageable(0, "qno");

        Page<QnA> result = qnARepository.findAll(qnARepository.makePredicate(vo.getType(),vo.getKeyword()), page);

        log.info(""+ page);
        log.info(""+result);

        log.info("TOTAL PAGE NUMBER: " + result.getTotalPages());
        model.addAttribute("result", new PageMaker<>(result));

    }

    @GetMapping("/view")
    public void view(Long qno, @ModelAttribute("pageVO") PageVO pageVO,Model model){
        qnARepository.findById(qno).ifPresent(qna ->
                model.addAttribute("vo",qna));
    }

    @GetMapping("/register")
    public void register(){

    }

    @PostMapping("/register")
    public String postRegister(@ModelAttribute("pageVO") PageVO pageVO, QnA qnA){
        qnARepository.save(qnA);
        return "redirect:/qna/list";
    }

    @GetMapping("/modify")
    public void modify(Long qno, @ModelAttribute("pageVO")PageVO pageVO,Model model){
        qnARepository.findById(qno).ifPresent(qnA -> model.addAttribute("vo",qnA));
    }
    @PostMapping("/modify")
    public String modifyPost(QnA qnA, PageVO vo, RedirectAttributes rttr ){
        qnARepository.findById(qnA.getQno()).ifPresent( origin ->{
            origin.setQnaKinds(qnA.getQnaKinds());
            origin.setTitle(qnA.getTitle());
            origin.setContent(qnA.getContent());
            qnARepository.save(origin);
            rttr.addFlashAttribute("msg", "success");
            rttr.addAttribute("qno", origin.getQno());
        });

        rttr.addAttribute("page", vo.getPage());
        rttr.addAttribute("size", vo.getSize());
        rttr.addAttribute("type", vo.getType());
        rttr.addAttribute("keyword", vo.getKeyword());

        return "redirect:/qna/view";
    }
    @PostMapping("/delete")
    public String delete(Long qno, PageVO pageVO, RedirectAttributes rttr){
        qnARepository.deleteById(qno);

        rttr.addFlashAttribute("msg","deletesuccess");
        rttr.addAttribute("page", pageVO.getPage());
        rttr.addAttribute("size", pageVO.getSize());
        rttr.addAttribute("type", pageVO.getType());
        rttr.addAttribute("keyword", pageVO.getKeyword());
        return "redirect:/qna/list";
    }
}

