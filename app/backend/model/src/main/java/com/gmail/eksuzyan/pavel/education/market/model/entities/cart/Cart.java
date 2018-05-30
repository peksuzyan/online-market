package com.gmail.eksuzyan.pavel.education.market.model.entities.cart;

import com.gmail.eksuzyan.pavel.education.market.model.entities.customer.Customer;
import com.gmail.eksuzyan.pavel.education.market.model.entities.Identifiable;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Cart")
public class Cart implements Identifiable, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public Long getPk() {
        return id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime created;

    @Column
    private LocalDateTime modified;

    @Column
    private boolean expired;

    @OneToOne
    private Customer customer;

    @ElementCollection
    @CollectionTable(name = "Cart_Entry", joinColumns = @JoinColumn(name = "cart_id"))
    private List<CartEntry> entries;

}
