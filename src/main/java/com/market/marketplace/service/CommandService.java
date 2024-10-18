package com.market.marketplace.service;

import com.market.marketplace.entities.Command;

import java.util.List;

public interface CommandService {
    List<Command> getCommandsForClient(int clientId);

    Command findCommandById(int id);

    boolean cancelCommand(int commandId);

    void updateCommandStatus(int commandId, String statusParam);

     List<Command> getAllCommandsOrderedByLatest();

    }
