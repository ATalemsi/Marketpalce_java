package com.market.marketplace.dao;

import com.market.marketplace.entities.Product;

import java.util.Optional;

public interface ProductDao {
    public Optional<Product> findById(int id);
}
