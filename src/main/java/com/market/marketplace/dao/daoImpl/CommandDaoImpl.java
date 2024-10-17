package com.market.marketplace.dao.daoImpl;

import com.market.marketplace.dao.CommandDao;
import com.market.marketplace.entities.Command;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class CommandDaoImpl implements CommandDao {
    private EntityManager entityManager;

    public CommandDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Command> getCommandsByClientId(int clientId) {
        String jpql = "SELECT c FROM Command c WHERE c.client.id = :clientId"; // Assuming there's a client field in Command
        TypedQuery<Command> query = entityManager.createQuery(jpql, Command.class);
        query.setParameter("clientId", clientId);
        return query.getResultList();
    }
}
