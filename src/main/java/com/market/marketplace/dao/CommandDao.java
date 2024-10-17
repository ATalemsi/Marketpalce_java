package com.market.marketplace.dao;

import com.market.marketplace.entities.Command;

import java.util.List;

public interface CommandDao {
    List<Command> getCommandsByClientId(int clientId);
}
