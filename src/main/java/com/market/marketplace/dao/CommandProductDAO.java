package com.market.marketplace.dao;

import com.market.marketplace.entities.CommandProduct;

import java.util.List;

public interface CommandProductDAO {
    void addCommandProduct(CommandProduct commandProduct);

    List<CommandProduct> findCurrentCartForClient(int clientId);

    public void deleteByCommandId(int commandId);

}
