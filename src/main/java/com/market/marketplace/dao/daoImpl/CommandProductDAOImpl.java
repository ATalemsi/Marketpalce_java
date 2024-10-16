package com.market.marketplace.dao.daoImpl;

import com.market.marketplace.dao.CommandProductDAO;
import com.market.marketplace.entities.CommandProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class CommandProductDAOImpl implements CommandProductDAO {

    private static final Logger logger = LoggerFactory.getLogger(CommandProductDAOImpl.class);
    private EntityManager entityManager;

    public CommandProductDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void addCommandProduct(CommandProduct commandProduct) {

        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            entityManager.persist(commandProduct);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
