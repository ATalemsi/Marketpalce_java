package com.market.marketplace.dao.daoImpl;

import com.market.marketplace.dao.ProductDao;
import com.market.marketplace.entities.Product;
import com.market.marketplace.util.JpaUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductDaoImpl implements ProductDao {

    private static final Logger logger = LoggerFactory.getLogger(ProductDaoImpl.class);

    @Override
    public void save(Product product) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = null;

        try {
            tx = em.getTransaction();
            tx.begin();
            em.persist(product);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

   @Override
    public Product findById(int id) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.find(Product.class, id);
        } finally {
            em.close();
        }
    }


    @Override
    public List<Product> findAll(int page, int size) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("SELECT p FROM Product p", Product.class)
                    .setFirstResult(page * size)
                    .setMaxResults(size)
                    .getResultList();
        } finally {
            em.close();
        }
    }

   @Override
    public void update(Product product) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = null;

        try {
            tx = em.getTransaction();
            tx.begin();
            em.merge(product);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Product product) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = null;

        try {
            tx = em.getTransaction();
            tx.begin();
            product = em.find(Product.class, product.getId());
            if (product != null) {
                em.remove(product);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public int countProducts() {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            return ((Number) em.createQuery("SELECT COUNT(p) FROM Product p").getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Product> searchByName(String name, int page, int size) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        TypedQuery<Product> query = em.createQuery("SELECT p FROM Product p WHERE p.name LIKE :name", Product.class);
        query.setParameter("name", "%" + name + "%");
        query.setFirstResult(page * size);
        query.setMaxResults(size);
        List<Product> results = query.getResultList();
        em.close();
        return results;
    }



}
