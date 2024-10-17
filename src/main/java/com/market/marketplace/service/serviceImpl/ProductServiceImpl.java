package com.market.marketplace.service.serviceImpl;

import com.market.marketplace.dao.ProductDao;
import com.market.marketplace.entities.Product;

public class ProductServiceImpl {

    private final ProductDao productDao;

    public ProductServiceImpl(ProductDao productDao) {
        this.productDao = productDao;
    }

    public void addProduct(Product product) {
         productDao.save(product);
    }
}
