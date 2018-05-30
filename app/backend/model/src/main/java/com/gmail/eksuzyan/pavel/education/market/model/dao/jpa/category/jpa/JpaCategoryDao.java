package com.gmail.eksuzyan.pavel.education.market.model.dao.jpa.category.jpa;

import com.gmail.eksuzyan.pavel.education.market.model.entities.product.Category;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.persistence.TransactionRequiredException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import static com.gmail.eksuzyan.pavel.education.market.model.entities.product.Category.CATEGORY_READ_ALL;
import static com.gmail.eksuzyan.pavel.education.market.model.entities.product.Category.CATEGORY_READ_ALL_BY_PK;
import static java.util.Objects.requireNonNull;

public class JpaCategoryDao {

    private EntityManager em;

    public JpaCategoryDao(EntityManager em) {
        this.em = em;
    }

    /**
     * Gets an entity by primary key.
     *
     * @param pk primary key
     * @return entity
     * @throws NullPointerException if primary key is null
     */
    public Optional<Category> read(Long pk) {
        requireNonNull(pk);

        return Optional.ofNullable(em.find(getClazz(), pk));
    }

    /**
     * Creates an entity.
     *
     * @param entity entity
     * @return primary key
     * @throws NullPointerException         if entity is null
     * @throws PersistenceException         if entity is detached
     * @throws TransactionRequiredException if transaction isn't active
     */
    public Long create(Category entity) {
        requireNonNull(entity);

        em.persist(entity);
        em.flush();

        return entity.getPk();
    }

    /**
     * Creates or updates an entity if specified by primary key found.
     *
     * @param entity entity
     * @return managed entity
     * @throws NullPointerException         if entity is null
     * @throws TransactionRequiredException if transaction isn't active
     */
    public Category createOrUpdate(Category entity) {
        requireNonNull(entity);

        Category merged = em.merge(entity);
        em.flush();

        return merged;
    }

    /**
     * Updates an entity.
     *
     * @param pk      primary key
     * @param updater updater
     * @throws NullPointerException         if primary key is null
     * @throws EntityNotFoundException      if entity wasn't found by primary key
     * @throws TransactionRequiredException if transaction isn't active
     */
    public void update(Long pk, Consumer<Category> updater) {
        requireNonNull(pk);
        requireNonNull(updater);

        Category managed = getManaged(pk);
        updater.accept(managed);
        em.flush();
    }

    /**
     * Deletes an entity.
     *
     * @param entity entity
     * @throws NullPointerException         if entity or its primary key is null
     * @throws EntityNotFoundException      if entity wasn't found
     * @throws TransactionRequiredException if transaction isn't active
     */
    public void delete(Category entity) {
        requireNonNull(entity);

        delete(entity.getPk());
    }

    /**
     * Deletes an entity by primary key.
     *
     * @param pk primary key
     * @throws NullPointerException         if primary key is null
     * @throws EntityNotFoundException      if entity wasn't found
     * @throws TransactionRequiredException if transaction isn't active
     */
    public void delete(Long pk) {
        requireNonNull(pk);

        Category managed = getManaged(pk);
        em.remove(managed);
        em.flush();
    }

    /**
     * Deletes a list of entities by specified primary key set.
     *
     * @param pks primary key set
     * @return the number of deleted entities
     * @throws NullPointerException         if pks is null or empty
     * @throws IllegalArgumentException     if pks contains null
     * @throws TransactionRequiredException if transaction isn't active
     */
    @Deprecated
    public int deleteAll(Set<Long> pks) {
        if (pks == null || pks.isEmpty())
            throw new NullPointerException("PKs cannot be null or empty. ");

        if (pks.stream().anyMatch(Objects::isNull))
            throw new IllegalArgumentException("PKs can't contain null. ");

        return em.createQuery("DELETE FROM Category AS c WHERE c.id IN :pks")
                .setParameter("pks", pks)
                .executeUpdate();
    }

    /**
     * Gets a list of entities by specified primary key set.
     *
     * @param pks primary key set
     * @return a list of entities
     * @throws NullPointerException     if pks is null or empty
     * @throws IllegalArgumentException if pks contains null
     */
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
     */
    public List<Category> readAllFromRange(int skip, int limit) {
        if (skip < 0 || limit < 0)
            throw new IllegalArgumentException(
                    "Arguments cannot be negative: skip=" + skip + ", limit=" + limit);

        return em.createNamedQuery(CATEGORY_READ_ALL, getClazz())
                .setFirstResult(skip)
                .setMaxResults(limit)
                .getResultList();
    }

    /**
     * Makes an entity manageable by current persistence context.
     *
     * @param pk primary key
     * @return manageable entity
     * @throws EntityNotFoundException if entity wasn't found
     */
    protected Category getManaged(Long pk) {
        return em.getReference(getClazz(), pk);
    }

    /**
     * Returns class object of entity which belongs to this dao.
     *
     * @return class object
     */
    protected Class<Category> getClazz() {
        return Category.class;
    }
}
