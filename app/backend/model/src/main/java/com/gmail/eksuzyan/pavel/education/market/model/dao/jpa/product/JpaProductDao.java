package com.gmail.eksuzyan.pavel.education.market.model.dao.jpa.product;

import com.gmail.eksuzyan.pavel.education.market.model.dao.jpa.JpaGenericDao;
import com.gmail.eksuzyan.pavel.education.market.model.dao.contract.product.ProductDao;
import com.gmail.eksuzyan.pavel.education.market.model.entities.product.Product;

import javax.persistence.EntityManager;

import static com.gmail.eksuzyan.pavel.education.market.model.entities.product.Product.PRODUCT_READ_ALL;
import static com.gmail.eksuzyan.pavel.education.market.model.entities.product.Product.PRODUCT_READ_ALL_BY_PK;

public class JpaProductDao extends JpaGenericDao<Long, Product> implements ProductDao {

    public JpaProductDao(EntityManager em) {
        super(em);
    }

    /**
     * Returns class object of entity which belongs to this dao.
     *
     * @return class object
     */
    @Override
    protected Class<Product> getClazz() {
        return Product.class;
    }

    /**
     * Gets a name of query for reading all entities by PK defined for this entity.
     *
     * @return a name of query
     */
    @Override
    protected String getReadAllByPkQueryName() {
        return PRODUCT_READ_ALL_BY_PK;
    }

    /**
     * Gets a name of query for reading all entities defined for this entity.
     *
     * @return a name of query
     */
    @Override
    protected String getReadAllQueryName() {
        return PRODUCT_READ_ALL;
    }
}
