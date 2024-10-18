package com.market.marketplace.dao.daoImpl;

import com.market.marketplace.dao.UserDao;
import com.market.marketplace.entities.Client;
import com.market.marketplace.entities.User;

import javax.persistence.*;
import java.util.List;

public class ClientDaoImpl implements UserDao {
    private EntityManagerFactory emf;

    public ClientDaoImpl(EntityManagerFactory entityManagerFactory) {
        this.emf = entityManagerFactory;
    }
        @Override
    public boolean validateUser(String email, String password) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery("SELECT u From User u WHERE u.email = :email AND u.password = :password", User.class);
            query.setParameter("email", email);
            query.setParameter("password", password);
             List<User> users = query.getResultList();
             return !users.isEmpty();
        }finally {
            em.close();
        }
    }
    @Override
    public boolean registerUser(Client client) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(client);
            em.getTransaction().commit();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            em.getTransaction().rollback();
            return false;
        }finally {
            em.close();
        }
    }

    @Override
    public boolean userExists(String email) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<User>query = em.createQuery("SELECT u From User u WHERE u.email = :email", User.class);
            query.setParameter("email", email);
            List<User> users = query.getResultList();
            return !users.isEmpty();
        }finally {
            em.close();
        }
    }

    @Override
    public User getUserByEmail(String email) {
        EntityManager em = emf.createEntityManager();
        User user = null;
        try {
            user = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return user;
    }
}
