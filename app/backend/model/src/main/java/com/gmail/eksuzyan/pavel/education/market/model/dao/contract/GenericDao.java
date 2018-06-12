package com.gmail.eksuzyan.pavel.education.market.model.dao.contract;

import com.gmail.eksuzyan.pavel.education.market.model.entities.Identifiable;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Common interface to manipulate entity.
 *
 * @param <T> primary key type
 * @param <E> entity type
 */
public interface GenericDao<T extends Comparable<T>, E extends Identifiable<T>> {

    /**
     * Gets an entity by primary key.
     *
     * @param pk primary key
     * @return entity
     * @throws NullPointerException if primary key is null
     */
    Optional<E> read(T pk);

    /**
     * Creates an entity.
     *
     * @param entity entity
     * @return primary key
     * @throws NullPointerException if entity is null
     */
    T create(E entity);

    /**
     * Creates or updates an entity if specified by primary key found.
     *
     * @param entity entity
     * @return entity
     * @throws NullPointerException if entity is null
     */
    E createOrUpdate(E entity);

    /**
     * Updates an entity.
     *
     * @param pk      primary key
     * @param updater updater
     * @throws NullPointerException if primary key is null
     */
    void update(T pk, Consumer<E> updater);

    /**
     * Deletes an entity.
     *
     * @param entity entity
     * @throws NullPointerException if entity or its primary key is null
     */
    void delete(E entity);

    /**
     * Deletes an entity by primary key.
     *
     * @param pk primary key
     * @throws NullPointerException if primary key is null
     */
    void delete(T pk);

    /**
     * Gets a list of entities by specified primary key set.
     *
     * @param pks primary key set
     * @return a list of entities
     * @throws NullPointerException     if pks is null or empty
     * @throws IllegalArgumentException if pks contains null
     */
    List<E> readAll(Set<T> pks);

    /**
     * Gets a list of entities skipping a number of first and retrieving at most a limit number.
     *
     * @param skip  a number of records skipped before retrieving by the query
     * @param limit a max number of records retrieved by the query
     * @return a list of entities
     * @throws IllegalArgumentException if any of arg is negative
     */
    List<E> readAll(int skip, int limit);

}
