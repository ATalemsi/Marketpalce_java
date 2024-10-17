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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        Product product = new Product("Test Product","description",10.0,2, admin);
        productService.addProduct(product);
        verify(productDao, times(1)).save(product);
    }
}
