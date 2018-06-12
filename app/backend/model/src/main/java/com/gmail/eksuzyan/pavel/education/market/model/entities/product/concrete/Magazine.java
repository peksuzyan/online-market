package com.gmail.eksuzyan.pavel.education.market.model.entities.product.concrete;

import com.gmail.eksuzyan.pavel.education.market.model.entities.product.Product;

import javax.persistence.*;

import static com.gmail.eksuzyan.pavel.education.market.model.entities.product.concrete.Magazine.MAGAZINE_READ_ALL;
import static com.gmail.eksuzyan.pavel.education.market.model.entities.product.concrete.Magazine.MAGAZINE_READ_ALL_BY_PK;

@Entity
@DiscriminatorValue("magazine")
@NamedQueries({
        @NamedQuery(
                name = MAGAZINE_READ_ALL_BY_PK,
//                query = "SELECT b FROM Book AS b WHERE b.category = 'book' AND b.id IN :pks ORDER BY b.id ASC"),
                query = "SELECT b FROM Magazine AS b WHERE b.id IN :pks ORDER BY b.id ASC"),
        @NamedQuery(
                name = MAGAZINE_READ_ALL,
//                query = "SELECT b FROM Book AS b WHERE b.category = 'book' ORDER BY b.id ASC")
                query = "SELECT b FROM Magazine AS b ORDER BY b.id ASC")
})
public class Magazine extends Product {

    public static final String MAGAZINE_READ_ALL_BY_PK = "Magazine.readAllByPk";
    public static final String MAGAZINE_READ_ALL = "Magazine.readAll";

    @Column
    private String publisher;

    @Column
    private Short pages;

    @Column
    private Byte rotation;

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Short getPages() {
        return pages;
    }

    public void setPages(Short pages) {
        this.pages = pages;
    }

    public Byte getRotation() {
        return rotation;
    }

    public void setRotation(Byte period) {
        this.rotation = period;
    }
}
