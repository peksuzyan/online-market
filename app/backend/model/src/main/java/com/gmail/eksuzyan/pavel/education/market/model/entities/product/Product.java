package com.gmail.eksuzyan.pavel.education.market.model.entities.product;

import com.gmail.eksuzyan.pavel.education.market.model.entities.Identifiable;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Product")
public class Product implements Identifiable, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public Long getPk() {
        return id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private double price;

    @Column
    private boolean disabled;

    @ManyToOne
    private Category category;

    @Embedded
    private Stock stock;

}
