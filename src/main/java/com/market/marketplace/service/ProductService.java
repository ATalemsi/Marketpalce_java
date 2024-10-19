package com.market.marketplace.service;

import com.market.marketplace.entities.Product;
import java.util.List;

public interface ProductService {

    // Method to add a new product
    void addProduct(Product product);

    // Method to retrieve a product by its ID
    Product getProductById(int id);

    // Method to retrieve all products
    List<Product> getAllProducts(int page, int size);

    // Method to modify an existing product
    void updateProduct(Product product);

    // Method to remove a product
    void removeProduct(Product product);

    int getTotalProductsCount();

    List<Product> searchProductsByName(String name, int page, int size);
    int getTotalProductsCountByAdmin(int adminId);

    List<Product> getAllProductsByAdmin(int adminId, int page, int size);

    List<Product> searchProductsByNameAndAdmin(String name, int adminId, int page, int size);


}
