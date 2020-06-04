package com.web.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString(exclude = {"carts","orders"})
@Entity
@Table(name = "tbl_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uNum;

    @NotNull
    @Column(unique = true)
    private String userId;
    @NotNull
    private String UserPw;
    @NotNull
    private String userName;
    @NotNull
    private String userPhone;
    @NotNull
    private String userEmail;
    @NotNull
    private String postNum;
    @NotNull
    private String addr1;
    @NotNull
    private String addr2;

    private int point;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "user")
    private List<UserRole> roles;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<Cart> carts;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<Order> orders;
}
