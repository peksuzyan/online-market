package com.gmail.eksuzyan.pavel.education.market.model.entities.address;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column
    private String country;

    @Column
    private String region;

    @Column
    private String city;

    @Column
    private String street;

    @Column
    private String house;

    @Column
    private Integer apartment;

}
