package com.market.marketplace.dao;

import com.market.marketplace.entities.Command;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import java.util.List;

public interface CommandDao {
    List<Command> getCommandsByClientId(int clientId);

    Command findById(int id);

    void update(Command command) ;
}
