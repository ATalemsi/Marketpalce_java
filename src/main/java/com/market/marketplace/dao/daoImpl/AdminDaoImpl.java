package com.market.marketplace.dao.daoImpl;

import com.market.marketplace.dao.AdminDao;
import com.market.marketplace.entities.Admin;
import com.market.marketplace.entities.Client;
import com.market.marketplace.entities.User;
import com.market.marketplace.entities.enums.Role;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

public class AdminDaoImpl implements AdminDao {
    private EntityManager entityManager;

    public AdminDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Admin> findAllAdmins() {
        try {
            TypedQuery<Admin> query = entityManager.createQuery("SELECT a FROM Admin a  WHERE a.accessLevel = 0", Admin.class);

            return query.getResultList();
        } catch (PersistenceException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Admin> findSuperAdmins(int page, int size) {

        try {
            TypedQuery<Admin> query = entityManager.createQuery("SELECT a FROM Admin a WHERE a.accessLevel = 1", Admin.class);
            query.setFirstResult((page - 1) * size); // Calculate start position
            query.setMaxResults(size);
            return query.getResultList();
        } catch (PersistenceException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public long countSuperAdmins() {
        // JPQL query to count all super admins (access level = 1)
        String jpql = "SELECT COUNT(a) FROM Admin a WHERE a.accessLevel = 1";

        TypedQuery<Long> countQuery = entityManager.createQuery(jpql, Long.class);

        return countQuery.getSingleResult(); // Return the total count of super admins
    }

    @Override
    public List<Client> findAllClients() {

        try {
            TypedQuery<Client> query = entityManager.createQuery("SELECT c FROM Client c", Client.class);
            return query.getResultList();
        } catch (PersistenceException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void assignRole(int userId, int niveauAcces) {

        try {
            Admin user = entityManager.find(Admin.class, userId);
            if (user != null) {

                entityManager.getTransaction().begin();
                user.setAccessLevel(niveauAcces);
                entityManager.merge(user);
                entityManager.getTransaction().commit();
            } else {
                // Handle the case where the user is not found
                System.out.println("User not found with ID: " + userId);
            }
        } catch (PersistenceException e) {

            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback(); // Rollback if the transaction is active
            }
            e.printStackTrace();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void addAdminNormal(Admin admin) {
        admin.setAccessLevel(0); // Set accessLevel to 0 for normal admin
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(admin);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void updateAdminNormal(Admin admin) {
        if (admin.getAccessLevel() == 0) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                Admin existingAdmin = entityManager.find(Admin.class, admin.getId());
                if (existingAdmin != null) {
                    existingAdmin.setFirstName(admin.getFirstName());
                    existingAdmin.setLastName(admin.getLastName());
                    existingAdmin.setEmail(admin.getEmail());
                    if (admin.getPassword() != null && !admin.getPassword().isEmpty()) {
                        existingAdmin.setPassword(admin.getPassword());
                    }
                    entityManager.merge(existingAdmin);
                }
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
        } else {
            System.out.println("This admin is not a SuperAdmin.");
        }
    }


    @Override
    public void deleteAdminNormal(int adminId) {
        Admin admin = entityManager.find(Admin.class, adminId);
        if (admin != null && admin.getAccessLevel() == 0) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                entityManager.remove(admin);
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
        } else {
            System.out.println("Normal Admin not found with ID: " + adminId);
        }
    }

    @Override
    public void addSuperAdmin(Admin admin) {
        admin.setAccessLevel(1); // Set accessLevel to 1 for super admin
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(admin);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void updateSuperAdmin(Admin admin) {
        if (admin.getAccessLevel() == 1) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                Admin existingAdmin = entityManager.find(Admin.class, admin.getId());
                if (existingAdmin != null) {
                    existingAdmin.setFirstName(admin.getFirstName());
                    existingAdmin.setLastName(admin.getLastName());
                    existingAdmin.setEmail(admin.getEmail());
                    if (admin.getPassword() != null) {
                        existingAdmin.setPassword(admin.getPassword());
                    }
                    entityManager.merge(existingAdmin);
                } else {
                    System.out.println("SuperAdmin not found with ID: " + admin.getId());
                }
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback(); // Rollback in case of error
                }
                e.printStackTrace();
            }
        } else {
            System.out.println("This admin is not a SuperAdmin.");
        }
    }

    @Override
    public void deleteSuperAdmin(int adminId) {
        Admin admin = entityManager.find(Admin.class, adminId);
        if (admin != null && admin.getAccessLevel() == 1) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                entityManager.remove(admin);
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
        } else {
            System.out.println("SuperAdmin not found with ID: " + adminId);
        }
    }

    public Admin getAdminInfoByEmail(String email) {
        Admin admin = null;
        try {
            admin = entityManager.createQuery("SELECT a FROM Admin a WHERE a.email = :email", Admin.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            e.printStackTrace();
        }
        return admin;
    }

    @Override
    public void deleteClientById(int clientId) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Client client = entityManager.find(Client.class, clientId);  // Find the client by ID
            if (client != null) {
                entityManager.remove(client);  // Delete the client
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public List<Admin> findSuperAdminsByEmail(String email) {
        try {
            TypedQuery<Admin> query = entityManager.createQuery(
                    "SELECT a FROM Admin a WHERE a.email = :email AND a.accessLevel = 1", Admin.class);
            query.setParameter("email", email);
            return query.getResultList();
        } catch (NoResultException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Admin> findAdminsByEmail(String email) {
        try {
            TypedQuery<Admin> query = entityManager.createQuery(
                    "SELECT a FROM Admin a WHERE a.email = :email AND a.accessLevel = 0", Admin.class);
            query.setParameter("email", email);
            return query.getResultList();
        } catch (NoResultException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Client> findClientsByEmail(String email) {
        try {
            TypedQuery<Client> query = entityManager.createQuery(
                    "SELECT c FROM Client c WHERE c.email = :email", Client.class);
            query.setParameter("email", email);
            return query.getResultList();
        } catch (NoResultException e) {
            e.printStackTrace();
            return null;
        }
    }
}
