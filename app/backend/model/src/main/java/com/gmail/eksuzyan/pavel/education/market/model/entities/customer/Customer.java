package com.gmail.eksuzyan.pavel.education.market.model.entities.customer;

import com.gmail.eksuzyan.pavel.education.market.model.entities.Identifiable;
import com.gmail.eksuzyan.pavel.education.market.model.entities.address.Address;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Customer")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String email;

    @Column
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Embedded
    private Address address;

}
