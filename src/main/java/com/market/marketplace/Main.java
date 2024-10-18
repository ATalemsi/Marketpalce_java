package com.market.marketplace;

import com.market.marketplace.dao.daoImpl.AdminDaoImpl;
import com.market.marketplace.entities.User;
import com.market.marketplace.entities.enums.Role;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/marketPlace";
        String user = "postgres";
        String password = "Abdo@2023";

        // Test JDBC connection
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            if (connection != null) {
                System.out.println("Connected to the database via JDBC!");
            }
        } catch (SQLException e) {
            System.err.println("JDBC connection failed: " + e.getMessage());
            return; // Exit if JDBC connection fails
        }

        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        try {
            // Initialize EntityManagerFactory and EntityManager
            entityManagerFactory = Persistence.createEntityManagerFactory("marketPlace");
            entityManager = entityManagerFactory.createEntityManager();
            System.out.println("EntityManager created!");


        } catch (Exception e) {
            System.err.println("EntityManager operation failed: " + e.getMessage());
        } finally {
            // Clean up EntityManager and EntityManagerFactory
            if (entityManager != null) {
                entityManager.close();
                System.out.println("EntityManager closed.");
            }
            if (entityManagerFactory != null) {
                entityManagerFactory.close();
                System.out.println("EntityManagerFactory closed.");
            }
        }
    }
}