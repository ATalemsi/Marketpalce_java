package com.market.marketplace.dao.daoImpl;

import com.market.marketplace.dao.ProductDao;
import com.market.marketplace.entities.Product;

import javax.persistence.EntityManager;
import java.util.Optional;

public class ProductDaoImpl implements ProductDao {
    private EntityManager entityManager;

    public ProductDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    public Optional<Product> findById(int id) {
        Product product = entityManager.find(Product.class, id);
        return Optional.ofNullable(product);
    }

}
