package com.market.marketplace.service.serviceImpl;

import com.market.marketplace.dao.CommandDao;
import com.market.marketplace.entities.Command;
import com.market.marketplace.entities.CommandProduct;
import com.market.marketplace.service.CommandService;

import java.math.BigDecimal;
import java.util.List;

public class CommandServiceImpl implements CommandService {
    private CommandDao commandDAO;

    public CommandServiceImpl(CommandDao commandDAO) {
        this.commandDAO = commandDAO;
    }

    @Override
    public List<Command> getCommandsForClient(int clientId) {
        return commandDAO.getCommandsByClientId(clientId);
    }

}
