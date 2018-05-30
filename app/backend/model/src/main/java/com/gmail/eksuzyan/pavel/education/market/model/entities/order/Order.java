package com.gmail.eksuzyan.pavel.education.market.model.entities.order;

import com.gmail.eksuzyan.pavel.education.market.model.entities.address.Address;
import com.gmail.eksuzyan.pavel.education.market.model.entities.customer.Customer;
import com.gmail.eksuzyan.pavel.education.market.model.entities.Identifiable;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Orders")
public class Order implements Identifiable, Serializable {

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
    private LocalDateTime delivery;

    @Embedded
    private Address address;

    @OneToOne
    private Customer customer;

    @ElementCollection
    @CollectionTable(name = "Orders_Entry", joinColumns = @JoinColumn(name = "order_id"))
    private List<OrderEntry> entries;

}
