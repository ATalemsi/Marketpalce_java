package com.market.marketplace.entities;

import com.market.marketplace.entities.enums.CommandStatus;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
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
    private LocalDate orderDate;

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
        this.orderDate = LocalDate.now();
    }

    public Command(LocalDate orderDate, CommandStatus status, Client client) {
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

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
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
    public double getTotalAmount() {
        return commandProducts.stream()
                .mapToDouble(commandProduct -> commandProduct.getQuantity() * commandProduct.getProduct().getPrice()) // Assurez-vous que Product a un attribut 'price'
                .sum();
    }

    @Transient // Pour indiquer que cet attribut ne doit pas être persistant dans la base de données
    private String orderDateString;

    // Getters et Setters...

    public String getOrderDateString() {
        return orderDateString;
    }

    public void setOrderDateString(String orderDateString) {
        this.orderDateString = orderDateString;
    }
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
