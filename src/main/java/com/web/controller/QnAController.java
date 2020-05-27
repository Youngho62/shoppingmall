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

        Pageable page = vo.makePageable(0, "qGroup","qStep");

        Page<QnA> result = qnARepository.findAll(qnARepository.makePredicate(vo.getType(),vo.getKeyword()), page);

        log.info(""+ page);
        log.info(""+result);

        log.info("TOTAL PAGE NUMBER: " + result.getTotalPages());
        model.addAttribute("result", new PageMaker<>(result));

    }

    @GetMapping("/view")
    public void view(Long qno, @ModelAttribute("pageVO") PageVO pageVO,Model model){
        qnARepository.findById(qno).ifPresent(qna ->{
            qna.setQHit(qna.getQHit()+1);
            qnARepository.save(qna);
            model.addAttribute("vo",qna);
        });
    }

    @GetMapping("/register")
    public void register(){

    }

    @PostMapping("/register")
    public String postRegister(@ModelAttribute("pageVO") PageVO pageVO, QnA qnA,RedirectAttributes rttr){
        qnARepository.save(qnA);
        qnA.setQGroup(Math.toIntExact(qnA.getQno()));
        qnARepository.save(qnA);
        rttr.addFlashAttribute("msg", "success");
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
            rttr.addAttribute("qno", origin.getQno());
        });

        rttr.addAttribute("page", vo.getPage());
        rttr.addAttribute("size", vo.getSize());
        rttr.addAttribute("type", vo.getType());
        rttr.addAttribute("keyword", vo.getKeyword());
        rttr.addFlashAttribute("msg","modifysuccess");
        return "redirect:/qna/list";
    }
    @PostMapping("/delete")
    public String delete(Long qno, PageVO pageVO, RedirectAttributes rttr){
        QnA qnA=qnARepository.findById(qno).get();
        QnA originQnA=qnARepository.findById((long) qnA.getQGroup()).get();
        originQnA.setQStep(originQnA.getQStep()-1);
        qnARepository.save(originQnA);
        qnARepository.deleteById(qno);

        rttr.addFlashAttribute("msg","deletesuccess");
        rttr.addAttribute("page", pageVO.getPage());
        rttr.addAttribute("size", pageVO.getSize());
        rttr.addAttribute("type", pageVO.getType());
        rttr.addAttribute("keyword", pageVO.getKeyword());
        return "redirect:/qna/list";
    }

    @GetMapping("/reply")
    public void Reply(Long qno, @ModelAttribute("pageVO") PageVO pageVO,Model model){
        qnARepository.findById(qno).ifPresent(qna ->{
            model.addAttribute("qnA",qna);
        });
    }

    @PostMapping("/reply")
    public String postReply(@ModelAttribute("pageVO") PageVO pageVO, QnA qnA,String content2,Long replyQno,RedirectAttributes rttr){
        //초기 원글의 댓글 갯수를 증가
        QnA originQna=qnARepository.findById((long) qnA.getQGroup()).get();
        originQna.setQStep(originQna.getQStep()+1);
        qnARepository.save(originQna);

        //리플의 리플을 작성할 경우 (원글의 리플일경우는 생략)
        System.out.println("originQna.getQno(): "+originQna.getQno());
        System.out.println("replyQno: "+replyQno);
        QnA replyQna=qnARepository.findById(replyQno).get();
        System.out.println(originQna.getQno().equals(replyQno));
        System.out.println(!originQna.getQno().equals(replyQno));
        if(!originQna.getQno().equals(replyQno)){
            replyQna.setQStep(replyQna.getQStep()+1);
            replyQna.setQIndent(replyQna.getQIndent()+1);
            qnARepository.save(replyQna);
        }
        //새로운 리플의 정보
        qnA.setQIndent(replyQna.getQIndent());
        qnA.setQStep(qnA.getQStep());

        String str="ㄴ>";
        for(int i=0;i<replyQna.getQIndent();i++){
            str+="ㄴ>";
        }
        qnA.setTitle(str+qnA.getTitle());
        qnA.setContent(qnA.getContent()+"\n\n\n"+content2);
        qnARepository.save(qnA);
        rttr.addFlashAttribute("msg", "replysuccess");
        return "redirect:/qna/list";
    }

}

