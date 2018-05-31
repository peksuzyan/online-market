package com.gmail.eksuzyan.pavel.education.market.model.dao.jpa.category.jpa;

import com.gmail.eksuzyan.pavel.education.market.model.dao.jpa.JpaGenericDao;
import com.gmail.eksuzyan.pavel.education.market.model.dao.jpa.category.CategoryDao;
import com.gmail.eksuzyan.pavel.education.market.model.entities.product.Category;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.gmail.eksuzyan.pavel.education.market.model.entities.product.Category.CATEGORY_READ_ALL;
import static com.gmail.eksuzyan.pavel.education.market.model.entities.product.Category.CATEGORY_READ_ALL_BY_PK;

public class JpaCategoryDao extends JpaGenericDao<Long, Category> implements CategoryDao {

    public JpaCategoryDao(EntityManager em) {
        super(em);
    }

    /**
     * Returns class object of entity which belongs to this dao.
     *
     * @return class object
     */
    @Override
    protected Class<Category> getClazz() {
        return Category.class;
    }

    /**
     * Gets a list of entities by specified primary key set.
     *
     * @param pks primary key set
     * @return a list of entities
     * @throws NullPointerException     if pks is null or empty
     * @throws IllegalArgumentException if pks contains null
     */
    @Override
    public List<Category> readAll(Set<Long> pks) {
        if (pks == null || pks.isEmpty())
            throw new NullPointerException("PKs cannot be null or empty. ");

        if (pks.stream().anyMatch(Objects::isNull))
            throw new IllegalArgumentException("PKs can't contain null. ");

        return em.createNamedQuery(CATEGORY_READ_ALL_BY_PK, getClazz())
                .setParameter("pks", pks)
                .getResultList();
    }

    /**
     * Gets a list of entities skipping a number of first and retrieving at most a limit number.
     *
     * @param skip  a number of records skipped before retrieving by the query
     * @param limit a max number of records retrieved by the query
     * @return a list of entities
     * @throws IllegalArgumentException if any of arg is negative
     */
    @Override
    public List<Category> readAll(int skip, int limit) {
        if (skip < 0 || limit < 0)
            throw new IllegalArgumentException(
                    "Arguments cannot be negative: skip=" + skip + ", limit=" + limit);

        return em.createNamedQuery(CATEGORY_READ_ALL, getClazz())
                .setFirstResult(skip)
                .setMaxResults(limit)
                .getResultList();
    }

    /**
     * Deletes a list of entities by specified primary key set.
     *
     * @param pks primary key set
     * @return the number of deleted entities
     * @throws NullPointerException     if pks is null or empty
     * @throws IllegalArgumentException if pks contains null
     * @throws PersistenceException     if the delete query fails
     */
    @Deprecated
    @Override
    public int deleteAll(Set<Long> pks) {
        if (pks == null || pks.isEmpty())
            throw new NullPointerException("PKs cannot be null or empty. ");

        if (pks.stream().anyMatch(Objects::isNull))
            throw new IllegalArgumentException("PKs can't contain null. ");

        return em.createQuery("DELETE FROM Category AS c WHERE c.id IN :pks")
                .setParameter("pks", pks)
                .executeUpdate();
    }
}
