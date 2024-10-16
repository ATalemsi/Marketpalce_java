package com.market.marketplace.entities;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "Product name cannot be null")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    @Column(nullable = false, unique = true)
    private String name;

    @NotNull(message = "Description cannot be null")
    @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
    @Column(nullable = false)
    private String description;

    @NotNull(message = "Price cannot be null")
    @Digits(integer = 10, fraction = 2, message = "Price must be a valid decimal number with up to 2 decimal places")
    @Min(value = 0, message = "Price cannot be negative")
    @Column(nullable = false)
    private BigDecimal price;

    @Min(value = 0, message = "Stock cannot be negative")
    @Column(nullable = false)
    private int stock;

    // Many products can be managed by one admin
    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;


    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CommandProduct> commandProducts = new HashSet<>();

    // Constructors
    public Product() {
    }

    public Product(String name, String description, BigDecimal price, int stock, Admin admin) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.admin = admin;
    }

    public Product(int productId, String test_product, double v) {
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public Set<CommandProduct> getCommandProducts() {
        return commandProducts;
    }

    public void setCommandProducts(Set<CommandProduct> commandProducts) {
        this.commandProducts = commandProducts;
    }
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", admin=" + admin.getId() +
                '}';
    }
}
