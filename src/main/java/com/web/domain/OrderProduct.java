package com.web.domain;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString(exclude = "order")
@Entity
@Table(name = "tbl_orderproduct")
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long opno;

    @ManyToOne(fetch= FetchType.LAZY,cascade = CascadeType.ALL)
    private Order order;

    @OneToOne
    private Product product;

    private int count;
}
