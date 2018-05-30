package com.gmail.eksuzyan.pavel.education.market.model.entities.product;

import com.gmail.eksuzyan.pavel.education.market.model.entities.Identifiable;

import javax.persistence.*;
import java.io.Serializable;

import static com.gmail.eksuzyan.pavel.education.market.model.entities.product.Category.CATEGORY_READ_ALL_BY_PK;
import static com.gmail.eksuzyan.pavel.education.market.model.entities.product.Category.CATEGORY_READ_ALL;

@Entity
@Table(name = "Category")
@NamedQueries({
        @NamedQuery(
                name = CATEGORY_READ_ALL_BY_PK,
                query = "SELECT c FROM Category AS c WHERE c.id IN :pks ORDER BY c.id ASC"),
        @NamedQuery(
                name = CATEGORY_READ_ALL,
                query = "SELECT c FROM Category AS c ORDER BY c.id ASC")
})
public class Category implements Identifiable, Serializable {

    private static final long serialVersionUID = 1L;

    public static final String CATEGORY_READ_ALL_BY_PK = "Category.readAllByPk";
    public static final String CATEGORY_READ_ALL = "Category.readAll";

    @Override
    public Long getPk() {
        return id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
