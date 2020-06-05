package com.web.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@ToString(exclude = {"products","user"})
@Entity
@Table(name = "tbl_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ono;

    @ManyToOne(fetch=FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "order",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderProduct> products;

    private String destPostNum;
    private String destAddr;
    private String deliRequests;
    private int totalPrice;
    private int point;
    private boolean payed;

    @CreationTimestamp
    private Timestamp regdate;

}
