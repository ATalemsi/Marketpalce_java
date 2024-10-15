package com.market.marketplace.entities;

import com.market.marketplace.entities.enums.CommandStatus;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "commands")
public class Command {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "Order date cannot be null")
    private LocalDateTime orderDate;

    @NotNull(message = "Status cannot be null")
    @Enumerated(EnumType.STRING)
    private CommandStatus status;

    @NotNull(message = "Client cannot be null")
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToMany(mappedBy = "command", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CommandProduct> commandProducts = new HashSet<>();

    // Constructors
    public Command() {
        this.orderDate = LocalDateTime.now();
    }

    public Command(LocalDateTime orderDate, CommandStatus status, Client client) {
        this.orderDate = orderDate;
        this.status = status;
        this.client = client;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public CommandStatus getStatus() {
        return status;
    }

    public void setStatus(CommandStatus status) {
        this.status = status;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<CommandProduct> getCommandProducts() {
        return commandProducts;
    }

    public void setCommandProducts(Set<CommandProduct> commandProducts) {
        this.commandProducts = commandProducts;
    }

    public void addCommandProduct(CommandProduct commandProduct) {
        commandProducts.add(commandProduct);
        commandProduct.setCommand(this);
    }

    public void removeCommandProduct(CommandProduct commandProduct) {
        commandProducts.remove(commandProduct);
        commandProduct.setCommand(null);
    }

    // toString method for debugging
    @Override
    public String toString() {
        return "Command{" +
                "id=" + id +
                ", orderDate=" + orderDate +
                ", status=" + status +
                ", clientId=" + (client != null ? client.getId() : null) +
                '}';
    }
}
