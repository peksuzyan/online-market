package com.gmail.eksuzyan.pavel.education.market.model.util.data;

import com.gmail.eksuzyan.pavel.education.market.model.dao.jpa.category.JpaCategoryDao;
import com.gmail.eksuzyan.pavel.education.market.model.dao.jpa.product.JpaProductDao;
import com.gmail.eksuzyan.pavel.education.market.model.dao.jpa.product.concrete.JpaBookDao;
import com.gmail.eksuzyan.pavel.education.market.model.dao.jpa.product.concrete.JpaMagazineDao;
import com.gmail.eksuzyan.pavel.education.market.model.dao.jpa.product.concrete.JpaPictureDao;
import com.gmail.eksuzyan.pavel.education.market.model.entities.product.Category;
import com.gmail.eksuzyan.pavel.education.market.model.entities.product.Product;
import com.gmail.eksuzyan.pavel.education.market.model.entities.product.concrete.Book;
import com.gmail.eksuzyan.pavel.education.market.model.entities.product.concrete.Magazine;
import com.gmail.eksuzyan.pavel.education.market.model.entities.product.concrete.Picture;
import com.gmail.eksuzyan.pavel.education.market.model.util.suite.JpaDaoTestSuite;

import java.time.Month;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import static java.time.LocalDate.of;

public interface JpaDaoTestData {

    static Collection<JpaDaoTestSuite> generateTestData() {

        return Arrays.asList(
                new JpaDaoTestSuite<Long, Category>()
                        .setDaoGenerator(JpaCategoryDao::new)
                        .setPkGenerator(() -> 1L)
                        .setEntityGenerator(Category::new)
                        .setUpdaterGenerator(category -> category.setName("name"))
                        .setPredicateGenerator(category -> Objects.equals("name", category.getName()))
                        .setPkIncrementor(pk -> Long.sum(pk, 1L)),

                new JpaDaoTestSuite<Long, Product>()
                        .setDaoGenerator(JpaProductDao::new)
                        .setPkGenerator(() -> 1L)
                        .setEntityGenerator(Product::new)
                        .setUpdaterGenerator(product -> {
                            product.setTitle("title");
                            product.setDisabled(true);
                            product.setPrice(13.4);
                        })
                        .setPredicateGenerator(product ->
                                Objects.equals("title", product.getTitle())
                                        && Objects.equals(true, product.getDisabled())
                                        && Objects.equals(13.4, product.getPrice()))
                        .setPkIncrementor(pk -> Long.sum(pk, 1L)),

                new JpaDaoTestSuite<Long, Book>()
                        .setDaoGenerator(JpaBookDao::new)
                        .setPkGenerator(() -> 1L)
                        .setEntityGenerator(Book::new)
                        .setUpdaterGenerator(book -> {
                            book.setTitle("title");
                            book.setDisabled(true);
                            book.setPrice(13.4);
                            book.setAuthor("Jack London");
                            book.setDescription("So much interesting!");
                            book.setPages((short) 90);
                            book.setPublished(of(2001, Month.APRIL, 12));
                        })
                        .setPredicateGenerator(book ->
                                Objects.equals("title", book.getTitle())
                                        && Objects.equals(true, book.getDisabled())
                                        && Objects.equals(13.4, book.getPrice())
                                        && Objects.equals("Jack London", book.getAuthor())
                                        && Objects.equals("So much interesting!", book.getDescription())
                                        && Objects.equals((short) 90, book.getPages())
                                        && Objects.equals(of(2001, Month.APRIL, 12), book.getPublished()))
                        .setPkIncrementor(pk -> Long.sum(pk, 1L)),

                new JpaDaoTestSuite<Long, Magazine>()
                        .setDaoGenerator(JpaMagazineDao::new)
                        .setPkGenerator(() -> 1L)
                        .setEntityGenerator(Magazine::new)
                        .setUpdaterGenerator(magazine -> {
                            magazine.setTitle("title");
                            magazine.setDisabled(true);
                            magazine.setPrice(13.4);
                            magazine.setPublisher("O'RELLY");
                            magazine.setPages((short) 90);
                            magazine.setRotation((byte) 4);
                        })
                        .setPredicateGenerator(magazine ->
                                Objects.equals("title", magazine.getTitle())
                                        && Objects.equals(true, magazine.getDisabled())
                                        && Objects.equals(13.4, magazine.getPrice())
                                        && Objects.equals("O'RELLY", magazine.getPublisher())
                                        && Objects.equals((short) 90, magazine.getPages())
                                        && Objects.equals((byte) 4, magazine.getRotation()))
                        .setPkIncrementor(pk -> Long.sum(pk, 1L)),

                new JpaDaoTestSuite<Long, Picture>()
                        .setDaoGenerator(JpaPictureDao::new)
                        .setPkGenerator(() -> 1L)
                        .setEntityGenerator(Picture::new)
                        .setUpdaterGenerator(picture -> {
                            picture.setTitle("title");
                            picture.setDisabled(true);
                            picture.setPrice(13.4);
                            picture.setAuthor("Jack London");
                            picture.setPublished(of(2001, Month.APRIL, 12));
                        })
                        .setPredicateGenerator(picture ->
                                Objects.equals("title", picture.getTitle())
                                        && Objects.equals(true, picture.getDisabled())
                                        && Objects.equals(13.4, picture.getPrice())
                                        && Objects.equals("Jack London", picture.getAuthor())
                                        && Objects.equals(of(2001, Month.APRIL, 12), picture.getPublished()))
                        .setPkIncrementor(pk -> Long.sum(pk, 1L))

        );


    }

}
