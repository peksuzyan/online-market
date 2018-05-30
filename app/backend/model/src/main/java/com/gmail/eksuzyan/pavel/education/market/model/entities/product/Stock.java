package com.gmail.eksuzyan.pavel.education.market.model.entities.product;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class Stock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column
    private Integer amount;
}
