package com.gmail.eksuzyan.pavel.education.market.model.dao;

import com.gmail.eksuzyan.pavel.education.market.model.exceptions.DatabaseException;

public interface GenericDao<T> {

    Long save(T entity) throws DatabaseException;

    T read(Long id) throws DatabaseException;

    boolean remove(Long id) throws DatabaseException;

}
