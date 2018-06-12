package com.gmail.eksuzyan.pavel.education.market.model.dao.jpa.category;

import com.gmail.eksuzyan.pavel.education.market.model.dao.jpa.JpaGenericDao;
import com.gmail.eksuzyan.pavel.education.market.model.dao.contract.category.CategoryDao;
import com.gmail.eksuzyan.pavel.education.market.model.entities.product.Category;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
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
     * Gets a name of query for reading all entities by PK defined for this entity.
     *
     * @return a name of query
     */
    @Override
    protected String getReadAllByPkQueryName() {
        return CATEGORY_READ_ALL_BY_PK;
    }

    /**
     * Gets a name of query for reading all entities defined for this entity.
     *
     * @return a name of query
     */
    @Override
    protected String getReadAllQueryName() {
        return CATEGORY_READ_ALL;
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
