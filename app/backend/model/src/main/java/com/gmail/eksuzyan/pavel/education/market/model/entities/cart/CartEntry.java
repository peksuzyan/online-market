package com.gmail.eksuzyan.pavel.education.market.model.entities.cart;

import com.gmail.eksuzyan.pavel.education.market.model.entities.product.Product;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class CartEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer amount;

    @OneToOne
    private Product product;
}
