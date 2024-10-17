package com.market.marketplace.dao;

import com.market.marketplace.entities.Product;

import java.util.List;

public interface ProductDao {
    void save(Product product);
     Product findById(int id);
     List<Product> findAll(int page, int size);
     void update(Product product);
     void delete(Product product);
    int countProducts();

   List<Product> searchByName(String name, int page, int size);
}
