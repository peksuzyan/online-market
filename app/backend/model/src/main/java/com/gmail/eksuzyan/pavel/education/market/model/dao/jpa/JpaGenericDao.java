package com.gmail.eksuzyan.pavel.education.market.model.dao.jpa;

import com.gmail.eksuzyan.pavel.education.market.model.dao.contract.GenericDao;
import com.gmail.eksuzyan.pavel.education.market.model.entities.Identifiable;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

public abstract class JpaGenericDao<T extends Comparable<T>, E extends Identifiable<T>> implements GenericDao<T, E> {

    protected EntityManager em;

    protected JpaGenericDao(EntityManager em) {
        this.em = em;
    }

    /**
     * Gets an entity by primary key.
     *
     * @param pk primary key
     * @return entity
     * @throws NullPointerException if primary key is null
     */
    @Override
    public Optional<E> read(T pk) {
        requireNonNull(pk);

        return Optional.ofNullable(em.find(getClazz(), pk));
    }

    /**
     * Creates an entity.
     *
     * @param entity entity
     * @return primary key
     * @throws NullPointerException if entity is null
     * @throws PersistenceException if the create fails
     */
    @Override
    public T create(E entity) {
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
     * @throws NullPointerException if entity is null
     * @throws PersistenceException if the create or update fails
     */
    @Override
    public E createOrUpdate(E entity) {
        requireNonNull(entity);

        E merged = em.merge(entity);
        em.flush();

        return merged;
    }

    /**
     * Updates an entity.
     *
     * @param pk      primary key
     * @param updater updater
     * @throws NullPointerException    if primary key is null
     * @throws EntityNotFoundException if entity wasn't found by primary key
     * @throws PersistenceException    if the update fails
     */
    @Override
    public void update(T pk, Consumer<E> updater) {
        requireNonNull(pk);
        requireNonNull(updater);

        E managed = getManaged(pk);
        updater.accept(managed);
        em.flush();
    }

    /**
     * Deletes an entity.
     *
     * @param entity entity
     * @throws NullPointerException    if entity or its primary key is null
     * @throws EntityNotFoundException if entity wasn't found
     * @throws PersistenceException    if the delete fails
     */
    @Override
    public void delete(E entity) {
        requireNonNull(entity);

        delete(entity.getPk());
    }

    /**
     * Deletes an entity by primary key.
     *
     * @param pk primary key
     * @throws NullPointerException    if primary key is null
     * @throws EntityNotFoundException if entity wasn't found
     * @throws PersistenceException    if the delete fails
     */
    @Override
    public void delete(T pk) {
        requireNonNull(pk);

        E managed = getManaged(pk);
        em.remove(managed);
        em.flush();
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
    public List<E> readAll(Set<T> pks) {
        if (pks == null || pks.isEmpty())
            throw new NullPointerException("PKs cannot be null or empty. ");

        if (pks.stream().anyMatch(Objects::isNull))
            throw new IllegalArgumentException("PKs can't contain null. ");

        return em.createNamedQuery(getReadAllByPkQueryName(), getClazz())
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
    public List<E> readAll(int skip, int limit) {
        if (skip < 0 || limit < 0)
            throw new IllegalArgumentException(
                    "Arguments cannot be negative: skip=" + skip + ", limit=" + limit);

        return em.createNamedQuery(getReadAllQueryName(), getClazz())
                .setFirstResult(skip)
                .setMaxResults(limit)
                .getResultList();
    }

    /**
     * Returns class object of entity which belongs to this dao.
     *
     * @return class object
     */
    protected abstract Class<E> getClazz();

    /**
     * Gets a name of query for reading all entities by PK defined for this entity.
     *
     * @return a name of query
     */
    protected abstract String getReadAllByPkQueryName();

    /**
     * Gets a name of query for reading all entities defined for this entity.
     *
     * @return a name of query
     */
    protected abstract String getReadAllQueryName();

    /**
     * Makes an entity manageable by current persistence context.
     *
     * @param pk primary key
     * @return manageable entity
     * @throws EntityNotFoundException if entity wasn't found
     */
    private E getManaged(T pk) {
        return em.getReference(getClazz(), pk);
    }

}
