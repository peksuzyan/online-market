package com.gmail.eksuzyan.pavel.education.market.model.dao.jpa.product.jpa;

import com.gmail.eksuzyan.pavel.education.market.model.dao.jpa.JpaGenericDao;
import com.gmail.eksuzyan.pavel.education.market.model.dao.jpa.product.ProductDao;
import com.gmail.eksuzyan.pavel.education.market.model.entities.product.Product;

import javax.persistence.EntityManager;

public class JpaProductDao extends JpaGenericDao<Product> implements ProductDao {

    public JpaProductDao(EntityManager em) {
        super(em);
    }

    @Override
    protected Class<Product> getClazz() {
        return Product.class;
    }
}
