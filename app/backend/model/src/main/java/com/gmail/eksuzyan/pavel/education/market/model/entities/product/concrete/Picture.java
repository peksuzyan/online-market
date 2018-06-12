package com.gmail.eksuzyan.pavel.education.market.model.entities.product.concrete;

import com.gmail.eksuzyan.pavel.education.market.model.converters.LocalDateAttributeConverter;
import com.gmail.eksuzyan.pavel.education.market.model.entities.product.Product;

import javax.persistence.*;
import java.time.LocalDate;

import static com.gmail.eksuzyan.pavel.education.market.model.entities.product.concrete.Picture.PICTURE_READ_ALL;
import static com.gmail.eksuzyan.pavel.education.market.model.entities.product.concrete.Picture.PICTURE_READ_ALL_BY_PK;

@Entity
@DiscriminatorValue("picture")
@NamedQueries({
        @NamedQuery(
                name = PICTURE_READ_ALL_BY_PK,
//                query = "SELECT b FROM Book AS b WHERE b.category = 'book' AND b.id IN :pks ORDER BY b.id ASC"),
                query = "SELECT b FROM Picture AS b WHERE b.id IN :pks ORDER BY b.id ASC"),
        @NamedQuery(
                name = PICTURE_READ_ALL,
//                query = "SELECT b FROM Book AS b WHERE b.category = 'book' ORDER BY b.id ASC")
                query = "SELECT b FROM Picture AS b ORDER BY b.id ASC")
})
public class Picture extends Product {

    public static final String PICTURE_READ_ALL_BY_PK = "Picture.readAllByPk";
    public static final String PICTURE_READ_ALL = "Picture.readAll";

    @Column
    private String author;

    @Column
    @Convert(converter = LocalDateAttributeConverter.class)
    private LocalDate published;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getPublished() {
        return published;
    }

    public void setPublished(LocalDate published) {
        this.published = published;
    }
}
