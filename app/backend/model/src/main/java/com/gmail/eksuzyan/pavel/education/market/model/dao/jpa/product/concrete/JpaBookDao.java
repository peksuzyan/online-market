package com.gmail.eksuzyan.pavel.education.market.model.dao.jpa.product.concrete;

import com.gmail.eksuzyan.pavel.education.market.model.dao.contract.product.concrete.BookDao;
import com.gmail.eksuzyan.pavel.education.market.model.dao.jpa.JpaGenericDao;
import com.gmail.eksuzyan.pavel.education.market.model.entities.product.concrete.Book;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import java.util.Collections;

import static com.gmail.eksuzyan.pavel.education.market.model.entities.product.concrete.Book.BOOK_READ_ALL;
import static com.gmail.eksuzyan.pavel.education.market.model.entities.product.concrete.Book.BOOK_READ_ALL_BY_PK;
import static java.util.Objects.requireNonNull;

public class JpaBookDao extends JpaGenericDao<Long, Book> implements BookDao {

    public JpaBookDao(EntityManager em) {
        super(em);
    }

    /**
     * Returns class object of entity which belongs to this dao.
     *
     * @return class object
     */
    @Override
    protected Class<Book> getClazz() {
        return Book.class;
    }

    /**
     * Gets a name of query for reading all entities by PK defined for this entity.
     *
     * @return a name of query
     */
    @Override
    protected String getReadAllByPkQueryName() {
        return BOOK_READ_ALL_BY_PK;
    }

    /**
     * Gets a name of query for reading all entities defined for this entity.
     *
     * @return a name of query
     */
    @Override
    protected String getReadAllQueryName() {
        return BOOK_READ_ALL;
    }

    /**
     * Deletes an entity by primary key.
     *
     * @param pk primary key
     * @throws NullPointerException    if primary key is null
     * @throws EntityNotFoundException if entity wasn't found
     * @throws PersistenceException    if the delete fails
     * @implSpec Makes sure primary key determines exactly this product. Due to performance efforts it'll be better to use
     * {@link com.gmail.eksuzyan.pavel.education.market.model.dao.contract.product.ProductDao#delete(Comparable)}
     */
    @Override
    public void delete(Long pk) {
        requireNonNull(pk);

        Book managed = readAll(Collections.singleton(pk))
                .stream()
                .findFirst()
                .orElseThrow(EntityNotFoundException::new);

        em.remove(managed);
        em.flush();
    }
}
