package com.gmail.eksuzyan.pavel.education.market.model.entities.product.concrete;

import com.gmail.eksuzyan.pavel.education.market.model.converters.LocalDateAttributeConverter;
import com.gmail.eksuzyan.pavel.education.market.model.entities.product.Product;

import javax.persistence.*;
import java.time.LocalDate;

import static com.gmail.eksuzyan.pavel.education.market.model.entities.product.concrete.Book.BOOK_READ_ALL;
import static com.gmail.eksuzyan.pavel.education.market.model.entities.product.concrete.Book.BOOK_READ_ALL_BY_PK;

@Entity
@DiscriminatorValue("book")
@NamedQueries({
        @NamedQuery(
                name = BOOK_READ_ALL_BY_PK,
//                query = "SELECT b FROM Book AS b WHERE b.category = 'book' AND b.id IN :pks ORDER BY b.id ASC"),
                query = "SELECT b FROM Book AS b WHERE b.id IN :pks ORDER BY b.id ASC"),
        @NamedQuery(
                name = BOOK_READ_ALL,
//                query = "SELECT b FROM Book AS b WHERE b.category = 'book' ORDER BY b.id ASC")
                query = "SELECT b FROM Book AS b ORDER BY b.id ASC")
})
public class Book extends Product {

    public static final String BOOK_READ_ALL_BY_PK = "Book.readAllByPk";
    public static final String BOOK_READ_ALL = "Book.readAll";

//    @Column(insertable = false, updatable = false)
//    private String category;

    @Column
    private String author;

    @Column
    private String description;

    @Column
    private Short pages;

    @Column
    @Convert(converter = LocalDateAttributeConverter.class)
    private LocalDate published;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Short getPages() {
        return pages;
    }

    public void setPages(Short pages) {
        this.pages = pages;
    }

    public LocalDate getPublished() {
        return published;
    }

    public void setPublished(LocalDate published) {
        this.published = published;
    }

//    public String getCategory() {
//        return category;
//    }
//
//    public void setCategory(String category) {
//        this.category = category;
//    }
}
