package com.market.marketplace.service.serviceImpl;

import com.market.marketplace.dao.ProductDao;
import com.market.marketplace.entities.Product;
import com.market.marketplace.service.ProductService;

import java.util.List;

public class ProductServiceImpl implements ProductService {

    private ProductDao productDao;

    // Constructor injection for the DAO dependency
    public ProductServiceImpl(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public void addProduct(Product product) {
        productDao.save(product);
    }

    @Override
    public Product getProductById(int id) {
        return productDao.findById(id);
    }

    @Override
    public List<Product> getAllProducts(int page, int size) {
        return productDao.findAll(page, size);
    }

    @Override
    public void updateProduct(Product product) {
        productDao.update(product);
    }

    @Override
    public void removeProduct(Product product) {
        productDao.delete(product);
    }

    @Override
    public int getTotalProductsCount() { return productDao.countProducts(); }

    @Override
    public List<Product> searchProductsByName(String name, int page, int size) {
        return productDao.searchByName(name, page, size);
    }
}
