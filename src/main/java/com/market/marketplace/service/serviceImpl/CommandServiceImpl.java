package com.market.marketplace.service.serviceImpl;

import com.market.marketplace.dao.CommandDao;
import com.market.marketplace.entities.Command;
import com.market.marketplace.entities.enums.CommandStatus;
import com.market.marketplace.service.CommandService;

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

    @Override
    public Command findCommandById(int id) {
        return commandDAO.findById(id);
    }

    public boolean cancelCommand(int commandId) {
        Command command = commandDAO.findById(commandId);
        if (command != null && (command.getStatus() == CommandStatus.PENDING || command.getStatus() == CommandStatus.PROCESSING)) {
            command.setStatus(CommandStatus.CANCELED);
            commandDAO.update(command);
            return true;
        }
        return false;
    }


    @Override
    public void updateCommandStatus(int commandId, String statusParam) {
        Command command = commandDAO.findById(commandId);
        if (command != null) {
            command.setStatus(CommandStatus.valueOf(statusParam));
            commandDAO.update(command);
        }
    }

    @Override
    public List<Command> getAllCommandsOrderedByLatest() {
        return commandDAO.getAllCommandsOrderedByLatest();
    }
}
