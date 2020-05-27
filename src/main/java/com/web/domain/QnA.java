package com.web.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "tbl_qna")
@EqualsAndHashCode(of = "qno")
@ToString
public class QnA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qno;
    private String qnaKinds;
    private String title;
    private String writer;
    private String content;
    @ColumnDefault("0")
    private int qHit;
    private int qGroup;
    @ColumnDefault("0")
    private int qStep;
    private int qIndent;

    @CreationTimestamp
    private Timestamp regdate;

}
