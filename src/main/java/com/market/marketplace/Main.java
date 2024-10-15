package com.market.marketplace;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        // JDBC connection details
        String url = "jdbc:postgresql://localhost:5432/marketPlace";
        String user = "postgres";
        String password = "password";

        // Test JDBC connection
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            if (connection != null) {
                System.out.println("Connected to the database!");
            }
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
        }

        // Initialize EntityManagerFactory
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("marketPlace");
        EntityManager entityManager = null;

        try {
            // Create an EntityManager
            entityManager = entityManagerFactory.createEntityManager();
            System.out.println("EntityManager created!");


        } catch (Exception e) {
            System.err.println("EntityManager initialization failed: " + e.getMessage());
        } finally {
            if (entityManager != null) {
                entityManager.close();
                System.out.println("EntityManager closed.");
            }
            entityManagerFactory.close();
            System.out.println("EntityManagerFactory closed.");
        }
    }
}
