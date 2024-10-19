package com.market.marketplace.dao;

import com.market.marketplace.entities.Command;
import com.market.marketplace.entities.enums.CommandStatus;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import java.time.LocalDate;
import java.util.List;

public interface CommandDao {
    List<Command> getCommandsByClientId(int clientId);

    Command findById(int id);

    void update(Command command) ;

    public List<Command> getAllCommandsOrderedByLatest() ;


    List<Command> findByIdOrStatus(Integer id, String status);
}
