package com.market.marketplace.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "command_product")
public class CommandProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "Quantity cannot be null")
    private int quantity;

    @NotNull(message = "Command cannot be null")
    @ManyToOne
    @JoinColumn(name = "command_id", nullable = false)
    private Command command;

    @NotNull(message = "Product cannot be null")
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Constructors
    public CommandProduct() {
    }

    public CommandProduct(int quantity, Command command, Product product) {
        this.quantity = quantity;
        this.command = command;
        this.product = product;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "CommandProduct{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", commandId=" + (command != null ? command.getId() : null) +
                ", productId=" + (product != null ? product.getId() : null) +
                '}';
    }
}
