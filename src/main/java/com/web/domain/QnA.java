package com.web.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tbl_qna")
@EqualsAndHashCode(of = "qno")
@ToString(exclude = "replies")
public class QnA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qno;
    private String qnaKinds;
    private String title;
    private String writer;
    private String content;

    @CreationTimestamp
    private Timestamp regdate;

    @OneToMany(mappedBy = "qnA")
    private List<QnAReply> replies;
}
