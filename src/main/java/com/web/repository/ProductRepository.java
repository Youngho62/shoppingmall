package com.web.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.web.domain.Product;
import com.web.domain.QProduct;
import com.web.domain.QnA;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product,Long>, QuerydslPredicateExecutor<Product> {
    @Query(nativeQuery = true, value = "select * from tbl_product order by p_hit desc limit ?")
    List<Product> findAllSortByPHit(int cnt);


    public default Predicate makePredicate(String keyword){

        BooleanBuilder builder = new BooleanBuilder();

        QProduct product = QProduct.product;

        builder.and(product.pno.gt(0));

        if(keyword == null){
            return builder;
        }

        builder.and(product.category.like("%" + keyword +"%"));

        return builder;
    }
}
