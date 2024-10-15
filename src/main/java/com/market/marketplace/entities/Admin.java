package com.market.marketplace.entities;

import com.market.marketplace.entities.enums.Role;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User {

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();

    @Column(nullable = false)
    private int accessLevel;

    // Constructors
    public Admin() {}

    public Admin(String firstName, String lastName, String email, String password, int accessLevel, Role role) {
        super(firstName, lastName, email, password, Role.ADMIN);
        this.accessLevel = accessLevel;
    }

    // Getters and Setters
    public int getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }
    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void addProduct(Product product) {
        products.add(product);
        product.setAdmin(this);
    }

    public void removeProduct(Product product) {
        products.remove(product);
        product.setAdmin(null);
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + getId() +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", role=" + getRole() +
                '}';
    }


}
