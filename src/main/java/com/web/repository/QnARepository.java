package com.web.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.web.domain.QQnA;
import com.web.domain.QnA;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

public interface QnARepository extends CrudRepository<QnA,Long>, QuerydslPredicateExecutor<QnA> {
    public default Predicate makePredicate(String type, String keyword){

        BooleanBuilder builder = new BooleanBuilder();

        QQnA qna = QQnA.qnA;

        builder.and(qna.qno.gt(0));

        if(type == null){
            return builder;
        }

        switch(type){
            case "t":
                builder.and(qna.title.like("%" + keyword +"%"));
                break;
            case "c":
                builder.and(qna.content.like("%" + keyword +"%"));
                break;
            case "w":
                builder.and(qna.writer.like("%" + keyword +"%"));
                break;
            case "k":
                builder.and(qna.qnaKinds.like("%" + keyword +"%"));
                break;
        }

        return builder;
    }
    public default Predicate searchType(String type, String keyword){
        BooleanBuilder builder=new BooleanBuilder();
        QQnA qna=QQnA.qnA;
        builder.and(qna.qno.gt(0));

        if(type==null)
            return builder;
        switch (type){
            case "t": builder.and(qna.title.like("%"+keyword+"%"));
                break;
            case "c": builder.and(qna.content.like("%"+keyword+"%"));
                break;
            case "w": builder.and(qna.writer.like("%"+keyword+"%"));
                break;
            case "k": builder.and(qna.qnaKinds.like("%"+keyword+"%"));
                break;
        }
        return builder;
    }
}
