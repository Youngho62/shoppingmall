package com.web;

import com.web.domain.Product;
import com.web.domain.QnA;
import com.web.repository.ProductFilesRepository;
import com.web.repository.ProductRepository;
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
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductFilesRepository productFilesRepository;

    @Test
    public void insert(){
        IntStream.range(181,210).forEach(i->{
            Product product=new Product();
            product.setCategory("가구");
            product.setDescription("Test setDescription입니다.."+i);
            product.setName("Test상품"+i);
            product.setPrice(5000);
            productRepository.save(product);
        });

    }
    @Test
    public void asdf(){
        productRepository.findAllSortByPHit(5).forEach(product -> {
            System.out.println(product.getName()+": "+product.getPHit());
        });

    }
    @Test
    public void assas(){
        productRepository.findAll().forEach(product -> {
            product.setMainPic(product.getMainPic().replace("\\","/"));
            productRepository.save(product);
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
