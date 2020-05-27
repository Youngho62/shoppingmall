package com.web;

import com.web.domain.QnA;
import com.web.repository.QnARepository;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.IntStream;

@RunWith(SpringRunner.class)
@Commit
@Log
@SpringBootTest
class ShoppingmallApplicationTests {
    @Autowired
    QnARepository qnARepository;

    @Test
    public void insert(){
        IntStream.range(1,50).forEach(i->{
            QnA qna=new QnA();
            qna.setTitle("Test: 결제 문의 "+i);
            qna.setWriter("rladudgh337");
            qna.setContent("Test: 결제 문의 내용"+i);
            qna.setQnaKinds("결제");
            qnARepository.save(qna);
        });

    }

    @Test
    public void testList1() {
        qnARepository.findAll().forEach(qnA -> {
            qnA.setQGroup(Math.toIntExact(qnA.getQno()));
            qnARepository.save(qnA);
        });
    }


    @Test
    public void testList2() {

        Pageable pageable = PageRequest.of(0, 20, Sort.Direction.DESC, "qno");

        Page<QnA> result = qnARepository.findAll(qnARepository.makePredicate(null, null), pageable);

        log.info("PAGE: " + result.getPageable());

        log.info("----------------------");
        result.getContent().forEach(qnA -> log.info("" + qnA));

    }

}
