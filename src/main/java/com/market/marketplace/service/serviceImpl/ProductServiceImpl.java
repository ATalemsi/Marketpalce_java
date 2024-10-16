package com.market.marketplace.service.serviceImpl;

import com.market.marketplace.dao.ProductDao;
import com.market.marketplace.entities.Product;
import com.market.marketplace.service.ProductService;

import java.util.Optional;

public class ProductServiceImpl  implements ProductService {
    private final ProductDao productDao;

    public ProductServiceImpl(ProductDao productDao) {
        this.productDao = productDao;
    }

    public Product getProductById(int productId) {
        return productDao.findById(productId).get();
    }

}


