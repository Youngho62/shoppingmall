package com.web.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@ToString(exclude = "files")
@Entity
@Table(name = "tbl_product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno;

    @NotNull
    @Column(unique = true)
    private String name;
    @NotNull
    private int price;
    @NotNull
    private String description;
    private String mainPic;
    @NotNull
    private String category;
    @ColumnDefault("0")
    private int pHit;
    @CreationTimestamp
    private Timestamp regDate;
    @CreationTimestamp
    private Timestamp updateDate;

    @OneToMany(mappedBy = "product",fetch = FetchType.LAZY)
    private List<ProductFiles> files;

}
