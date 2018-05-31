package com.gmail.eksuzyan.pavel.education.market.model.dao.jpa.category;

import com.gmail.eksuzyan.pavel.education.market.model.dao.GenericDao;
import com.gmail.eksuzyan.pavel.education.market.model.entities.product.Category;

import java.util.Set;

public interface CategoryDao extends GenericDao<Long, Category> {

    /**
     * Deletes a list of entities by specified primary key set.
     *
     * @param pks primary key set
     * @return the number of deleted entities
     * @throws NullPointerException     if pks is null or empty
     * @throws IllegalArgumentException if pks contains null
     */
    int deleteAll(Set<Long> pks);

}
