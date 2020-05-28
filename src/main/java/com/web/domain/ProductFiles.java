package com.web.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString(exclude = "product")
@Entity
@Table(name = "tbl_productfiles")
public class ProductFiles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fno;

    private String fileName;
    private String uploadUrl;
    private String uuid;

    private boolean mainPic;
    @JsonIgnore
    @ManyToOne(fetch=FetchType.LAZY)
    private Product product;
}
