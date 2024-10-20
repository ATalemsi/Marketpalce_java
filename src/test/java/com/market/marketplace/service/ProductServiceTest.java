package com.market.marketplace.service;

import com.market.marketplace.dao.ProductDao;
import com.market.marketplace.entities.Admin;
import com.market.marketplace.entities.Product;
import com.market.marketplace.service.serviceImpl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddProduct() {
        Admin admin = new Admin();
        admin.setAccessLevel(1);
        Product product = new Product("Test Product", "description", 10.0, 2, admin);
        productService.addProduct(product);
        verify(productDao, times(1)).save(product);
    }

    @Test
    void testGetProductById() {
        Product product = new Product("Test Product", "description", 10.0, 2, new Admin());
        when(productDao.findById(1)).thenReturn(product);

        Product result = productService.getProductById(1);
        assertEquals(product, result);
        verify(productDao, times(1)).findById(1);
    }

    @Test
    void testGetAllProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product("Test Product 1", "description 1", 10.0, 2, new Admin()));
        products.add(new Product("Test Product 2", "description 2", 20.0, 3, new Admin()));
        when(productDao.findAll(0, 10)).thenReturn(products);

        List<Product> result = productService.getAllProducts(0, 10);
        assertEquals(2, result.size());
        verify(productDao, times(1)).findAll(0, 10);
    }

    @Test
    void testUpdateProduct() {
        Product product = new Product("Test Product", "description", 10.0, 2, new Admin());
        productService.updateProduct(product);
        verify(productDao, times(1)).update(product);
    }

    @Test
    void testRemoveProduct() {
        Product product = new Product("Test Product", "description", 10.0, 2, new Admin());
        productService.removeProduct(product);
        verify(productDao, times(1)).delete(product);
    }

    @Test
    void testGetTotalProductsCount() {
        when(productDao.countProducts()).thenReturn(100);
        int count = productService.getTotalProductsCount();
        assertEquals(100, count);
        verify(productDao, times(1)).countProducts();
    }

    @Test
    void testSearchProductsByName() {
        List<Product> products = new ArrayList<>();
        products.add(new Product("Test Product", "description", 10.0, 2, new Admin()));
        when(productDao.searchByName("Test", 0, 10)).thenReturn(products);

        List<Product> result = productService.searchProductsByName("Test", 0, 10);
        assertEquals(1, result.size());
        verify(productDao, times(1)).searchByName("Test", 0, 10);
    }

    @Test
    void testGetAllProductsByAdmin() {
        List<Product> products = new ArrayList<>();
        products.add(new Product("Admin Product", "description", 15.0, 3, new Admin()));
        when(productDao.findAllByAdmin(1, 0, 10)).thenReturn(products);

        List<Product> result = productService.getAllProductsByAdmin(1, 0, 10);
        assertEquals(1, result.size());
        verify(productDao, times(1)).findAllByAdmin(1, 0, 10);
    }

    @Test
    void testGetTotalProductsCountByAdmin() {
        when(productDao.countProductsByAdmin(1)).thenReturn(50);
        int count = productService.getTotalProductsCountByAdmin(1);
        assertEquals(50, count);
        verify(productDao, times(1)).countProductsByAdmin(1);
    }

    @Test
    void testSearchProductsByNameAndAdmin() {
        List<Product> products = new ArrayList<>();
        products.add(new Product("Admin Product", "description", 15.0, 3, new Admin()));
        when(productDao.searchByNameAndAdmin("Admin", 1, 0, 10)).thenReturn(products);

        List<Product> result = productService.searchProductsByNameAndAdmin("Admin", 1, 0, 10);
        assertEquals(1, result.size());
        verify(productDao, times(1)).searchByNameAndAdmin("Admin", 1, 0, 10);
    }
}
