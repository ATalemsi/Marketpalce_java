package com.market.marketplace.dao.daoImpl;

import com.market.marketplace.dao.CommandDao;
import com.market.marketplace.entities.Command;
import com.market.marketplace.entities.enums.CommandStatus;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.*;
import java.time.LocalDate;
import java.util.List;

public class CommandDaoImpl implements CommandDao {
    private EntityManager entityManager;

    public CommandDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Command> getCommandsByClientId(int clientId) {
        entityManager.clear();
        String jpql = "SELECT c FROM Command c WHERE c.client.id = :clientId"; // Assuming there's a client field in Command
        TypedQuery<Command> query = entityManager.createQuery(jpql, Command.class);
        query.setParameter("clientId", clientId);
        return query.getResultList();
    }

    @Override
    public Command findById(int id) {
        entityManager.clear();
        return entityManager.find(Command.class, id);
    }

    @Override
    @Transactional
    public void update(Command command) {
        if (command != null) {
            entityManager.getTransaction().begin(); // Démarrer une transaction
            entityManager.merge(command);           // Utiliser merge pour mettre à jour l'entité
            entityManager.getTransaction().commit(); // Valider la transaction
        }
    }
    @Override

    public List<Command> getAllCommandsOrderedByLatest() {
        entityManager.clear();
        String jpql = "SELECT c FROM Command c ORDER BY c.orderDate DESC"; // Assurez-vous que orderDate est le bon champ
        TypedQuery<Command> query = entityManager.createQuery(jpql, Command.class);
        return query.getResultList();
    }


    @Override
    public List<Command> findByIdOrStatus(Integer id, String status) {
        String jpql = "SELECT c FROM Command c WHERE c.id = :id OR c.status = :status";
        TypedQuery<Command> query = entityManager.createQuery(jpql, Command.class);
        query.setParameter("id", id);
        query.setParameter("status", status);
        return query.getResultList();
    }
}
