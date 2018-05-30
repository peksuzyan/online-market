package com.gmail.eksuzyan.pavel.education.market.model.dao.jpa;

import com.gmail.eksuzyan.pavel.education.market.model.dao.GenericDao;
import com.gmail.eksuzyan.pavel.education.market.model.entities.Identifiable;
import com.gmail.eksuzyan.pavel.education.market.model.exceptions.DatabaseException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

public abstract class JpaGenericDao<T extends Identifiable> implements GenericDao<T> {

    protected EntityManager em;

    public JpaGenericDao(EntityManager em) {
        this.em = em;
    }

    @Override
    public Long save(T entity) throws DatabaseException {
        try {
            em.persist(entity);
            return entity.getPk();
        } catch (PersistenceException e) {
            throw new DatabaseException("Entity wasn't saved. ", e);
        }
    }

    @Override
    public T read(Long id) throws DatabaseException {
        try {
            return em.find(getClazz(), id);
        } catch (PersistenceException e) {
            throw new DatabaseException("Entity wasn't found. ", e);
        }
    }

    @Override
    public boolean remove(Long id) throws DatabaseException {
        try {
            T entity = em.find(getClazz(), id);
            em.remove(entity);
            return !em.contains(id);
        } catch (PersistenceException e) {
            throw new DatabaseException("Entity wasn't removed. ", e);
        }
    }

    protected abstract Class<T> getClazz();

}
