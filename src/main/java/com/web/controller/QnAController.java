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
import org.springframework.web.bind.annotation.RequestMapping;

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
    public void view(){

    }

}

