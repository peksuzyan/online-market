package com.gmail.eksuzyan.pavel.education.market.model.entities.product;

import com.gmail.eksuzyan.pavel.education.market.model.entities.Identifiable;

import javax.persistence.*;
import java.io.Serializable;

import static com.gmail.eksuzyan.pavel.education.market.model.entities.product.Product.PRODUCT_READ_ALL;
import static com.gmail.eksuzyan.pavel.education.market.model.entities.product.Product.PRODUCT_READ_ALL_BY_PK;

@Entity
@Table(name = "Product")
@Inheritance
@DiscriminatorColumn(name = "category")
@NamedQueries({
        @NamedQuery(
                name = PRODUCT_READ_ALL_BY_PK,
                query = "SELECT p FROM Product AS p WHERE p.id IN :pks ORDER BY p.id ASC"),
        @NamedQuery(
                name = PRODUCT_READ_ALL,
                query = "SELECT p FROM Product AS p ORDER BY p.id ASC")
})
public class Product implements Identifiable<Long>, Serializable {

    private static final long serialVersionUID = 1L;

    public static final String PRODUCT_READ_ALL_BY_PK = "Product.readAllByPk";
    public static final String PRODUCT_READ_ALL = "Product.readAll";

    @Override
    public Long getPk() {
        return getId();
    }

    @Override
    public void setPk(Long pk) {
        setId(pk);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private Double price;

    @Column
    private Boolean disabled;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }
}
