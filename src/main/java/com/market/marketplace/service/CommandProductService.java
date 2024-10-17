package com.market.marketplace.service;

import com.market.marketplace.entities.CommandProduct;

import java.util.List;

public interface CommandProductService {
    void addProductToCommand(int productId, int quantity, int commandId);

    List<CommandProduct> getCurrentCartForClient(int i);
}
