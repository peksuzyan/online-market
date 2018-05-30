package com.gmail.eksuzyan.pavel.education.market.model.entities.order;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class OrderEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private Integer amount;

    @Column
    private Double price;

}
