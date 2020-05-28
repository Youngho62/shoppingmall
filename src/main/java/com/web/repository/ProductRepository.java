package com.web.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.web.domain.Product;
import com.web.domain.QProduct;
import com.web.domain.QnA;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product,Long>, QuerydslPredicateExecutor<Product> {
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
