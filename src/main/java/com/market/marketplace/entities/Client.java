package com.market.marketplace.entities;

import com.market.marketplace.entities.enums.Role;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("CLIENT")
public class Client extends User {

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Command> commands = new ArrayList<>();


    @Size( min = 10, max = 255, message = "Shipping address must be between 10 and 255 characters")
    @Column()
    private String shippingAddress;


    @Size(min = 5, max = 50, message = "Payment method must be between 5 and 50 characters")
    @Column()
    private String paymentMethod;

    // Constructors
    public Client() {
    }

    public Client(String firstName, String lastName, String email, String password, String shippingAddress, String paymentMethod) {
        super(firstName, lastName, email, password, Role.CLIENT);
        this.shippingAddress = shippingAddress;
        this.paymentMethod = paymentMethod;
    }

    // Getters and Setters
    public List<Command> getCommands() {
        return commands;
    }

    public void setCommands(List<Command> commands) {
        this.commands = commands;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void addCommand(Command command) {
        commands.add(command);
        command.setClient(this);
    }

    public void removeCommand(Command command) {
        commands.remove(command);
        command.setClient(null);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + getId() +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", role=" + getRole() +
                ", shippingAddress='" + shippingAddress + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                '}';
    }
}
